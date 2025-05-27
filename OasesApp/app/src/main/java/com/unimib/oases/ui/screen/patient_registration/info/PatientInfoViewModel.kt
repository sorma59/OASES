package com.unimib.oases.ui.screen.patient_registration.info

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.model.PatientStatus
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.usecase.InsertPatientLocallyUseCase
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase
import com.unimib.oases.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    }



    init {
        savedStateHandle.get<String>("patientId")?.let { id ->
            if(id.isNotBlank()) {
                viewModelScope.launch(dispatcher + errorHandler) {
                    useCases.getPatient(id).also { result ->
                        result.collect { resource ->
                            when (resource) {
                                is Resource.Loading -> {
                                    _state.value = _state.value.copy(isLoading = true)
                                }

                                is Resource.Success -> {
                                    _state.value = _state.value.copy(
                                        patient = resource.data!!,
                                        isLoading = false
                                    )
                                    currentPatientId = id
                                }

                                is Resource.Error -> {
                                    _state.value = _state.value.copy(
                                        error = resource.message,
                                        isLoading = false
                                    )

                                }

                                is Resource.None -> {}
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
        // all events that gonna happen when we need to screen to display something and pass data back to the screen
        data class showSnackbar(val message: String) : UiEvent()
        object SavePatient : UiEvent()
        object Back : UiEvent()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: PatientInfoEvent) {
        when(event) {
            is PatientInfoEvent.NameChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(name = event.name), nameError = null)
            }
            is PatientInfoEvent.AgeChanged -> {
                _state.value = _state.value.copy(patient = _state.value.patient.copy(age = event.age), ageError = null)
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
            is PatientInfoEvent.Submit -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    try {
                        _state.value = _state.value.copy(isLoading = true)
                        submitData()
                        _state.value = _state.value.copy(isLoading = false)
                        _eventFlow.emit(UiEvent.SavePatient)

                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            isLoading = false
                        )
                        _eventFlow.emit(
                            UiEvent.showSnackbar(
                                message = e.message ?: "An error occurred"
                            )
                        )
                    }
                }
            }
        }
    }








    private fun submitData() {
        Log.d("PatientInfoViewModel", "submitData called, ${state.value.patient.name} ${state.value.patient.age}")
        val result = validatePatientInfoFormUseCase.invoke(
            name = state.value.patient.name,
            age = state.value.patient.age
        )

        if (!result.successful){
            _state.value = _state.value.copy(
                nameError = result.nameErrorMessage,
                ageError = result.ageErrorMessage
            )
            return
        }


        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            insertPatientLocallyUseCase(_state.value.patient)
            validationEventsChannel.send(ValidationEvent.Success)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}