package com.unimib.oases.ui.screen.patient_registration.triage.red_code

sealed class RedCodeEvent {
    data class UnconsciousnessChanged(val value: Boolean) : RedCodeEvent()
    data class ActiveConvulsionsChanged(val value: Boolean) : RedCodeEvent()
    data class RespiratoryDistressChanged(val value: Boolean) : RedCodeEvent()
    data class HeavyBleedingChanged(val value: Boolean) : RedCodeEvent()
    data class HighRiskTraumaBurnsChanged(val value: Boolean) : RedCodeEvent()
    data class ThreatenedLimbChanged(val value: Boolean) : RedCodeEvent()
    data class PoisoningIntoxicationChanged(val value: Boolean) : RedCodeEvent()
    data class SnakeBiteChanged(val value: Boolean) : RedCodeEvent()
    data class AggressiveBehaviorChanged(val value: Boolean) : RedCodeEvent()
    data class PregnancyHeavyBleedingChanged(val value: Boolean) : RedCodeEvent()
    data class SevereAbdominalPainChanged(val value: Boolean) : RedCodeEvent()
    data class SeizuresChanged(val value: Boolean) : RedCodeEvent()
    data class AlteredMentalStatusChanged(val value: Boolean) : RedCodeEvent()
    data class SevereHeadacheChanged(val value: Boolean) : RedCodeEvent()
    data class VisualChangesChanged(val value: Boolean) : RedCodeEvent()
    data class SbpHighDpbHighChanged(val value: Boolean) : RedCodeEvent()
    data class TraumaChanged(val value: Boolean) : RedCodeEvent()
    data class ActiveLaborChanged(val value: Boolean) : RedCodeEvent()
}