package com.unimib.oases.ui.screen.patient_registration.triage

sealed class TriageEvent {

    data class FieldToggled(val field: String) : TriageEvent()
    // Red Code
//    data class UnconsciousnessChanged(val value: Boolean) : TriageEvent()
//    data class ActiveConvulsionsChanged(val value: Boolean) : TriageEvent()
//    data class RespiratoryDistressChanged(val value: Boolean) : TriageEvent()
//    data class HeavyBleedingChanged(val value: Boolean) : TriageEvent()
//    data class HighRiskTraumaBurnsChanged(val value: Boolean) : TriageEvent()
//    data class ThreatenedLimbChanged(val value: Boolean) : TriageEvent()
//    data class PoisoningIntoxicationChanged(val value: Boolean) : TriageEvent()
//    data class SnakeBiteChanged(val value: Boolean) : TriageEvent()
//    data class AggressiveBehaviorChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithHeavyBleedingChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithSevereAbdominalPainChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithSeizuresChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithAlteredMentalStatusChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithSevereHeadacheChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithVisualChangesChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithTraumaChanged(val value: Boolean) : TriageEvent()
//    data class PregnancyWithActiveLaborChanged(val value: Boolean) : TriageEvent()
//
//    // Yellow Code
//    data class AirwaySwellingMassChanged(val value: Boolean): TriageEvent()
//    data class OngoingBleedingChanged(val value: Boolean): TriageEvent()
//    data class SeverePallorChanged(val value: Boolean): TriageEvent()
//    data class OngoingSevereVomitingDiarrheaChanged(val value: Boolean): TriageEvent()
//    data class UnableToFeedOrDrinkChanged(val value: Boolean): TriageEvent()
//    data class RecentFaintingChanged(val value: Boolean): TriageEvent()
//    data class LethargyConfusionAgitationChanged(val value: Boolean): TriageEvent()
//    data class FocalNeurologicVisualDeficitChanged(val value: Boolean): TriageEvent()
//    data class HeadacheWithStiffNeckChanged(val value: Boolean): TriageEvent()
//    data class SeverePainChanged(val value: Boolean): TriageEvent()
//    data class AcuteTesticularScrotalPainPriapismChanged(val value: Boolean): TriageEvent()
//    data class UnableToPassUrineChanged(val value: Boolean): TriageEvent()
//    data class AcuteLimbDeformityOpenFractureChanged(val value: Boolean): TriageEvent()
//    data class OtherTraumaBurnsChanged(val value: Boolean): TriageEvent()
//    data class SexualAssaultChanged(val value: Boolean): TriageEvent()
//    data class AnimalBiteNeedlestickPunctureChanged(val value: Boolean): TriageEvent()
//    data class OtherPregnancyRelatedComplaintsChanged(val value: Boolean): TriageEvent()

    object ToastShown: TriageEvent()
}