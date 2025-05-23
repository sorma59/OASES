package com.unimib.oases.ui.screen.patient_registration.info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.data.model.PatientStatus
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.usecase.InsertPatientLocallyUseCase
import com.unimib.oases.domain.usecase.ValidatePatientInfoFormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientInfoViewModel @Inject constructor(
    private val validatePatientInfoFormUseCase: ValidatePatientInfoFormUseCase,
    private val insertPatientLocallyUseCase: InsertPatientLocallyUseCase
): ViewModel() {

    private val _state = MutableStateFlow(PatientInfoState())
    val state: StateFlow<PatientInfoState> = _state.asStateFlow()

    private val validationEventsChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventsChannel.receiveAsFlow()

    fun onEvent(event: PatientInfoEvent) {
        when(event) {
            is PatientInfoEvent.NameChanged -> {
                _state.value = _state.value.copy(name = event.name, nameError = null)
            }
            is PatientInfoEvent.AgeChanged -> {
                _state.value = _state.value.copy(age = event.age, ageError = null)
            }
            is PatientInfoEvent.SexChanged -> {
                _state.value = _state.value.copy(sex = event.sex)
            }
            is PatientInfoEvent.VillageChanged -> {
                _state.value = _state.value.copy(village = event.village)
            }
            is PatientInfoEvent.ParishChanged -> {
                _state.value = _state.value.copy(parish = event.parish)
            }
            is PatientInfoEvent.SubCountyChanged -> {
                _state.value = _state.value.copy(subCounty = event.subCounty)
            }
            is PatientInfoEvent.DistrictChanged -> {
                _state.value = _state.value.copy(district = event.district)
            }
            is PatientInfoEvent.NextOfKinChanged -> {
                _state.value = _state.value.copy(nextOfKin = event.nextOfKin)
            }
            is PatientInfoEvent.ContactChanged -> {
                _state.value = _state.value.copy(contact = event.contact)
            }
            is PatientInfoEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        Log.d("PatientInfoViewModel", "submitData called, ${state.value.name} ${state.value.age}")
        val result = validatePatientInfoFormUseCase.invoke(
            name = state.value.name,
            age = state.value.age
        )

        if (!result.successful){
            _state.value = _state.value.copy(
                nameError = result.nameErrorMessage,
                ageError = result.ageErrorMessage
            )
            return
        }

        val patient = Patient(
            name = state.value.name,
            age = state.value.age,
            sex = state.value.sex,
            village = state.value.village,
            parish = state.value.parish,
            subCounty = state.value.subCounty,
            district = state.value.district,
            nextOfKin = state.value.nextOfKin,
            status = PatientStatus.WAITING_FOR_TRIAGE.name,
            contact = state.value.contact
        )

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            insertPatientLocallyUseCase(patient)
            validationEventsChannel.send(ValidationEvent.Success)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}