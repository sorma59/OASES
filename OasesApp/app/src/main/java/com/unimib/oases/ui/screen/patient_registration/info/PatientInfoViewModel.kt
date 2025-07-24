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
import kotlinx.coroutines.flow.update
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

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.update{
            _state.value.copy(
                error = e.message,
                isLoading = false
            )
        }
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
                                    _state.update{
                                        _state.value.copy(
                                            isLoading = true
                                        )
                                    }
                                }

                                is Resource.Success -> {
                                    val patient = resource.data!!
                                    _state.update{
                                        _state.value.copy(
                                            patient = patient,
                                            isLoading = false
                                        )
                                    }
                                }

                                is Resource.Error -> {
                                    _state.update{
                                        _state.value.copy(
                                            error = resource.message,
                                            isLoading = false
                                        )
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            } else {
                _state.update{
                    _state.value.copy(
                        isLoading = false,
                        patient = _state.value.patient
                    )
                }
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
                _state.update{
                    _state.value.copy(patient = _state.value.patient.copy(name = event.name), nameError = null, edited = true)
                }
            }

            is PatientInfoEvent.BirthDateChanged -> {
                val ageInMonths = DateTimeFormatter().calculateAgeInMonths(event.birthDate)
                _state.update{
                    if (ageInMonths != null)
                        _state.value.copy(
                            patient = _state.value.patient.copy(ageInMonths = ageInMonths),
                            birthDateError = null
                        )
                    else
                        _state.value.copy(
                            patient = _state.value.patient.copy(birthDate = event.birthDate),
                            birthDateError = null,
                            edited = true
                        )
                }
            }

            is PatientInfoEvent.AgeChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(ageInMonths = event.ageInMonths),
                        birthDateError = null,
                        edited = true
                    )
                }
                val newBirthDate = DateTimeFormatter().calculateBirthDate(event.ageInMonths)
                if (newBirthDate != null)
                    _state.update{
                        _state.value.copy(patient = _state.value.patient.copy(birthDate = newBirthDate))
                    }
            }

            is PatientInfoEvent.SexChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(sex = event.sex),
                        sexError = null,
                        edited = true
                    )
                }
            }

            is PatientInfoEvent.VillageChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(village = event.village),
                        edited = true
                    )
                }
            }

            is PatientInfoEvent.ParishChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(parish = event.parish),
                        edited = true
                    )
                }
            }

            is PatientInfoEvent.SubCountyChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(subCounty = event.subCounty),
                        edited = true
                    )
                }
            }

            is PatientInfoEvent.DistrictChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(district = event.district),
                        edited = true
                    )
                }
            }

            is PatientInfoEvent.NextOfKinChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(nextOfKin = event.nextOfKin),
                        edited = true
                    )
                }
            }

            is PatientInfoEvent.ContactChanged -> {
                _state.update{
                    _state.value.copy(
                        patient = _state.value.patient.copy(contact = event.contact),
                        edited = true
                    )
                }
            }

            is PatientInfoEvent.ValidateForm -> {
                validateAndPrepareForSubmission()
            }

            is PatientInfoEvent.ConfirmSubmission -> {
                if (_state.value.edited)
                    savePatientData()
                else
                    viewModelScope.launch {
                        validationEventsChannel.send(ValidationEvent.SubmissionSuccess)
                    }
            }
        }
    }

    // Validates the form and prepares it for submission
    private fun validateAndPrepareForSubmission() {
        viewModelScope.launch(dispatcher + errorHandler) {

            // Validate
            val result = validatePatientInfoFormUseCase.invoke(
                name = _state.value.patient.name,
                birthDate = _state.value.patient.birthDate,
                sex = _state.value.patient.sex
            )

            // Update state with errors
            _state.value = _state.value.copy(
                nameError = result.nameErrorMessage,
                birthDateError = result.birthDateErrorMessage,
                sexError = result.sexErrorMessage
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