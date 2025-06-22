package com.unimib.oases.ui.screen.patient_registration.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.usecase.InsertPatientLocallyUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase
import com.unimib.oases.util.DateTimeFormatter
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientInfoViewModel @Inject constructor(
    private val validatePatientInfoFormUseCase: ValidatePatientInfoFormUseCase,
    private val useCases: PatientUseCase,
    private val insertPatientLocallyUseCase: InsertPatientLocallyUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(PatientInfoState())
    val state: StateFlow<PatientInfoState> = _state.asStateFlow()

    private val validationEventsChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventsChannel.receiveAsFlow()

    private var currentPatientId: String? = null

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.ShowSnackbar(
                    message = e.message ?: "An unexpected error occurred during submission."
                )
            )
        }
    }

    init {
        savedStateHandle.get<String>("patientId")?.let { id ->
            if(id.isNotBlank()) {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.getPatient(id).also { result ->
                        result.collect { resource ->
                            when(resource){
                                is Resource.Loading -> {
                                    _state.value = _state.value.copy(
                                        isLoading = true
                                    )
                                }
                                is Resource.Success -> {
                                    val patient = resource.data!!
                                    _state.value = _state.value.copy(
                                        patient = patient,
                                        isLoading = false
                                    )
                                    currentPatientId = id
                                    if (patient.birthDate.isNotBlank()){
                                        val newAgeInMonths = DateTimeFormatter().calculateAgeInMonths(patient.birthDate)
                                        if (newAgeInMonths != null)
                                            _state.value.patient.copy(ageInMonths = newAgeInMonths)
                                    }
                                }
                                is Resource.Error -> {
                                    _state.value = _state.value.copy(
                                        error = resource.message,
                                        isLoading = false
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                }
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    patient = _state.value.patient
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()

    fun onEvent(event: PatientInfoEvent) {
        when(event) {
            is PatientInfoEvent.NameChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(name = event.name), nameError = null)
            }
            is PatientInfoEvent.BirthDateChanged -> {
                val ageInMonths = DateTimeFormatter().calculateAgeInMonths(event.birthDate)
                if (ageInMonths != null){
                    _state.value = _state.value.copy(patient = _state.value.patient.copy(ageInMonths = ageInMonths), ageError = null)
                }
                _state.value = _state.value.copy(patient = _state.value.patient.copy(birthDate = event.birthDate))
            }
            is PatientInfoEvent.AgeChanged -> {
                if (_state.value.patient.birthDate.isEmpty())
                    _state.value = _state.value.copy(patient = _state.value.patient.copy(ageInMonths = event.ageInMonths), ageError = null)
                //else
                    //TODO
            }
            is PatientInfoEvent.SexChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(sex = event.sex))
            }
            is PatientInfoEvent.VillageChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(village = event.village))
            }
            is PatientInfoEvent.ParishChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(parish = event.parish))
            }
            is PatientInfoEvent.SubCountyChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(subCounty = event.subCounty))
            }
            is PatientInfoEvent.DistrictChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(district = event.district))
            }
            is PatientInfoEvent.NextOfKinChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(nextOfKin = event.nextOfKin))
            }
            is PatientInfoEvent.ContactChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(contact = event.contact))
            }

            is PatientInfoEvent.ValidateForm -> {
                validateAndPrepareForSubmission()
            }

            is PatientInfoEvent.ConfirmSubmission -> {
                savePatientData()
            }

        }
    }

    // Validates the form and prepares it for submission
    private fun validateAndPrepareForSubmission() {
        viewModelScope.launch(dispatcher + errorHandler) {

            // Validate
            val result = validatePatientInfoFormUseCase.invoke(
                name = _state.value.patient.name,
                age = _state.value.patient.ageInMonths
            )

            // Update state with errors
            _state.value = _state.value.copy(
                nameError = result.nameErrorMessage,
                ageError = result.ageErrorMessage
            )

            // If there are errors, stop here
            if (!result.successful) {
                return@launch // Exit the function early
            }

            // Validation succeeded, communicate it to the UI
            validationEventsChannel.send(ValidationEvent.ValidationSuccess)
        }
    }

    private fun savePatientData() {
        viewModelScope.launch(dispatcher + errorHandler) {
            _state.value = _state.value.copy(isLoading = true)

            try {
                insertPatientLocallyUseCase(_state.value.patient)

                validationEventsChannel.send(ValidationEvent.SubmissionSuccess)

            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Failed to save patient data: ${e.message}"))
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    sealed class ValidationEvent {
        data object ValidationSuccess : ValidationEvent() // Successful validation
        data object SubmissionSuccess : ValidationEvent() // Successful submission to the db
    }
}