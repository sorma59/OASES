package com.unimib.oases.ui.screen.patient_registration.triage.non_red_code

sealed class NonRedCodeEvent {
    data class AirwaySwellingMassChanged(val value: Boolean): NonRedCodeEvent()
    data class OngoingBleedingChanged(val value: Boolean): NonRedCodeEvent()
    data class SeverePallorChanged(val value: Boolean): NonRedCodeEvent()
    data class OngoingSevereVomitingDiarrheaChanged(val value: Boolean): NonRedCodeEvent()
    data class UnableToFeedOrDrinkChanged(val value: Boolean): NonRedCodeEvent()
    data class RecentFaintingChanged(val value: Boolean): NonRedCodeEvent()
    data class LethargyConfusionAgitationChanged(val value: Boolean): NonRedCodeEvent()
    data class FocalNeurologicalVisualDeficitChanged(val value: Boolean): NonRedCodeEvent()
    data class HeadacheWithStiffNeckChanged(val value: Boolean): NonRedCodeEvent()
    data class SeverePainChanged(val value: Boolean): NonRedCodeEvent()
    data class AcuteTesticularScrotalPainPriapismChanged(val value: Boolean): NonRedCodeEvent()
    data class UnableToPassUrineChanged(val value: Boolean): NonRedCodeEvent()
    data class AcuteLimbDeformityOpenFractureChanged(val value: Boolean): NonRedCodeEvent()
    data class OtherTraumaBurnsChanged(val value: Boolean): NonRedCodeEvent()
    data class SexualAssaultChanged(val value: Boolean): NonRedCodeEvent()
    data class AnimalBiteNeedlestickPunctureChanged(val value: Boolean): NonRedCodeEvent()
    data class OtherPregnancyRelatedComplaintsChanged(val value: Boolean): NonRedCodeEvent()
}