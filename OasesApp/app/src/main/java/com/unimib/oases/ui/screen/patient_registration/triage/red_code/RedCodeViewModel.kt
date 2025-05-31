package com.unimib.oases.ui.screen.patient_registration.triage.red_code

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RedCodeViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(RedCodeState())
    val state: StateFlow<RedCodeState> = _state.asStateFlow()

    fun onEvent(event: RedCodeEvent){
        when(event){
            is RedCodeEvent.UnconsciousnessChanged -> {
                _state.value = _state.value.copy(unconsciousness = event.value)
            }
            is RedCodeEvent.ActiveConvulsionsChanged -> {
                _state.value = _state.value.copy(activeConvulsions = event.value)
            }
            is RedCodeEvent.RespiratoryDistressChanged -> {
                _state.value = _state.value.copy(respiratoryDistress = event.value)
            }
            is RedCodeEvent.HeavyBleedingChanged -> {
                _state.value = _state.value.copy(heavyBleeding = event.value)
            }
            is RedCodeEvent.HighRiskTraumaBurnsChanged -> {
                _state.value = _state.value.copy(highRiskTraumaBurns = event.value)
            }
            is RedCodeEvent.ThreatenedLimbChanged -> {
                _state.value = _state.value.copy(threatenedLimb = event.value)
            }
            is RedCodeEvent.PoisoningIntoxicationChanged -> {
                _state.value = _state.value.copy(poisoningIntoxication = event.value)
            }
            is RedCodeEvent.SnakeBiteChanged -> {
                _state.value = _state.value.copy(snakeBite = event.value)
            }
            is RedCodeEvent.AggressiveBehaviorChanged -> {
                _state.value = _state.value.copy(aggressiveBehavior = event.value)
            }
            is RedCodeEvent.PregnancyHeavyBleedingChanged -> {
                _state.value = _state.value.copy(pregnancyHeavyBleeding = event.value)
            }
            is RedCodeEvent.SevereAbdominalPainChanged -> {
                _state.value = _state.value.copy(severeAbdominalPain = event.value)
            }
            is RedCodeEvent.SeizuresChanged -> {
                _state.value = _state.value.copy(seizures = event.value)
            }
            is RedCodeEvent.AlteredMentalStatusChanged -> {
                _state.value = _state.value.copy(alteredMentalStatus = event.value)
            }
            is RedCodeEvent.SevereHeadacheChanged -> {
                _state.value = _state.value.copy(severeHeadache = event.value)
            }
            is RedCodeEvent.ActiveLaborChanged -> {
                _state.value = _state.value.copy(activeLabor = event.value)
            }
            is RedCodeEvent.SbpHighDpbHighChanged -> {
                _state.value = _state.value.copy(sbpHighDpbHigh = event.value)
            }
            is RedCodeEvent.TraumaChanged -> {
                _state.value = _state.value.copy(trauma = event.value)
            }
            is RedCodeEvent.VisualChangesChanged -> {
                _state.value = _state.value.copy(visualChanges = event.value)
            }
        }
    }

    fun updateVitalSigns(sbpValue: String, dbpValue: String) {
        val sbp = sbpValue.toIntOrNull() ?: 0
        val dbp = dbpValue.toIntOrNull() ?: 0
        _state.value = _state.value.copy(sbpHighDpbHigh = sbp >= 160 || dbp >= 110)
    }
}