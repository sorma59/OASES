package com.unimib.oases.ui.screen.patient_registration

import androidx.lifecycle.ViewModel
import com.unimib.oases.di.ApplicationScope
import com.unimib.oases.domain.repository.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

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

            is RegistrationEvent.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        TODO("Not yet implemented")
    }
}