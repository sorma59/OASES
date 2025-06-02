package com.unimib.oases.ui.screen.patient_registration.triage.non_red_code

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class NonRedCodeViewModel @Inject constructor(): ViewModel(){

    companion object {
        private const val EMPTY_FIELD = -1.0
    }

    private var _state = MutableStateFlow(NonRedCodeState())
    val state: StateFlow<NonRedCodeState> = _state.asStateFlow()

    fun onEvent(event: NonRedCodeEvent){
        when(event){
            is NonRedCodeEvent.AirwaySwellingMassChanged -> {
                _state.value = _state.value.copy(
                    airwaySwellingMass = event.value
                )
            }
            is NonRedCodeEvent.OngoingBleedingChanged -> {
                _state.value = _state.value.copy(
                    ongoingBleeding = event.value
                )
            }
            is NonRedCodeEvent.SeverePallorChanged -> {
                _state.value = _state.value.copy(
                    severePallor = event.value
                )
            }
            is NonRedCodeEvent.OngoingSevereVomitingDiarrheaChanged -> {
                _state.value = _state.value.copy(
                    ongoingSevereVomitingDiarrhea = event.value
                )
            }
            is NonRedCodeEvent.UnableToFeedOrDrinkChanged -> {
                _state.value = _state.value.copy(
                    unableToFeedOrDrink = event.value
                )
            }
            is NonRedCodeEvent.RecentFaintingChanged -> {
                _state.value = _state.value.copy(
                    recentFainting = event.value
                )
            }
            is NonRedCodeEvent.LethargyConfusionAgitationChanged -> {
                _state.value = _state.value.copy(
                    lethargyConfusionAgitation = event.value
                )
            }
            is NonRedCodeEvent.FocalNeurologicalVisualDeficitChanged -> {
                _state.value = _state.value.copy(
                    focalNeurologicVisualDeficit = event.value
                )
            }
            is NonRedCodeEvent.HeadacheWithStiffNeckChanged -> {
                _state.value = _state.value.copy(
                    headacheWithStiffNeck = event.value
                )
            }
            is NonRedCodeEvent.SeverePainChanged -> {
                _state.value = _state.value.copy(
                    severePain = event.value
                )
            }
            is NonRedCodeEvent.AcuteTesticularScrotalPainPriapismChanged -> {
                _state.value = _state.value.copy(
                    acuteTesticularScrotalPainPriapism = event.value
                )
            }
            is NonRedCodeEvent.UnableToPassUrineChanged -> {
                _state.value = _state.value.copy(
                    unableToPassUrine = event.value
                )
            }
            is NonRedCodeEvent.AcuteLimbDeformityOpenFractureChanged -> {
                _state.value = _state.value.copy(
                    acuteLimbDeformityOpenFracture = event.value
                )
            }
            is NonRedCodeEvent.OtherTraumaBurnsChanged -> {
                _state.value = _state.value.copy(
                    otherTraumaBurns = event.value
                )
            }
            is NonRedCodeEvent.SexualAssaultChanged -> {
                _state.value = _state.value.copy(
                    sexualAssault = event.value
                )
            }
            is NonRedCodeEvent.AnimalBiteNeedlestickPunctureChanged -> {
                _state.value = _state.value.copy(
                    animalBiteNeedlestickPuncture = event.value
                )
            }
            is NonRedCodeEvent.OtherPregnancyRelatedComplaintsChanged -> {
                _state.value = _state.value.copy(
                    otherPregnancyRelatedComplaints = event.value
                )
            }
        }
    }

    fun updateVitalSignsAndAge(ageInt: Int, spo2: String, rr: String, hr: String, sbp: String, temp: String){
        val spo2Int = spo2.toDoubleOrNull() ?: EMPTY_FIELD
        val rrInt = rr.toDoubleOrNull() ?: EMPTY_FIELD
        val hrInt = hr.toDoubleOrNull() ?: EMPTY_FIELD
        val sbpInt = sbp.toDoubleOrNull() ?: EMPTY_FIELD
        val tempDouble = temp.toDoubleOrNull() ?: EMPTY_FIELD

        _state.value = _state.value.copy(
            ageOver80Years = ageInt >= 80,
            alteredVitalSignsSpo2 = if (spo2Int != EMPTY_FIELD) spo2Int < 92 else state.value.alteredVitalSignsSpo2,
            alteredVitalSignsRrLow = if (rrInt != EMPTY_FIELD) rrInt < 10 else state.value.alteredVitalSignsRrLow,
            alteredVitalSignsRrHigh = if (rrInt != EMPTY_FIELD) rrInt > 30 else state.value.alteredVitalSignsRrHigh,
            alteredVitalSignsHrLow = if (hrInt != EMPTY_FIELD) hrInt < 50 else state.value.alteredVitalSignsHrLow,
            alteredVitalSignsHrHigh = if (hrInt != EMPTY_FIELD) hrInt > 130 else state.value.alteredVitalSignsHrHigh,
            alteredVitalSignsSbpLow = if (sbpInt != EMPTY_FIELD) sbpInt < 90 else state.value.alteredVitalSignsSbpLow,
            alteredVitalSignsSbpHigh = if (sbpInt != EMPTY_FIELD) sbpInt > 200 else state.value.alteredVitalSignsSbpHigh,
            alteredVitalSignsTempLow = if (tempDouble != EMPTY_FIELD) tempDouble < 35.0 else state.value.alteredVitalSignsTempLow,
            alteredVitalSignsTempHigh = if (tempDouble != EMPTY_FIELD) tempDouble > 39.0 else state.value.alteredVitalSignsTempHigh,
        )
    }

}