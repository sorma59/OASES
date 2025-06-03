package com.unimib.oases.ui.screen.patient_registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unimib.oases.di.ApplicationScope
import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.usecase.PatientUseCase
import com.unimib.oases.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val patientUseCase: PatientUseCase,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private var errorHandler = CoroutineExceptionHandler { _, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
//            isLoading = false
        )
    }


    fun onEvent(event: RegistrationEvent) {
        when (event) {

            is RegistrationEvent.PatientSubmitted -> {
                _state.value = _state.value.copy(
                    patientInfoState = event.patientInfoState
                )
            }

            is RegistrationEvent.PastMedicalHistoryNext -> {
                _state.value = _state.value.copy(
                    pastHistoryState = event.pastHistoryState
                )
            }

            is RegistrationEvent.VitalSignsSubmitted -> {
                _state.value = _state.value.copy(
                    vitalSignsState = event.vitalSignsState
                )
            }

            is RegistrationEvent.TriageCodeSelected -> {
                _state.value = _state.value.copy(
                    triageCode = event.triageCode
                )
            }

            is RegistrationEvent.Submit -> {
                val patient = _state.value.patientInfoState.patient
                val pastHistory = _state.value.pastHistoryState.diseases
                val vitalSigns = _state.value.vitalSignsState.vitalSigns.filter {it.value.isNotEmpty()}
                val triageCode = _state.value.triageCode

                viewModelScope.launch(dispatcher + errorHandler) {
                    patientUseCase.updateTriageState(patient , triageCode)
                }

            }
        }
    }
}