package com.unimib.oases.ui.screen.patient_registration.triage.red_code

sealed class RedCodeEvent {
    data class OnUnconsciousnessChanged(val value: Boolean) : RedCodeEvent()
    data class OnActiveConvulsionsChanged(val value: Boolean) : RedCodeEvent()
    data class OnRespiratoryDistressChanged(val value: Boolean) : RedCodeEvent()
    data class OnHeavyBleedingChanged(val value: Boolean) : RedCodeEvent()
    data class OnHighRiskTraumaBurnsChanged(val value: Boolean) : RedCodeEvent()
    data class OnThreatenedLimbChanged(val value: Boolean) : RedCodeEvent()
    data class OnPoisoningIntoxicationChanged(val value: Boolean) : RedCodeEvent()
    data class OnSnakeBiteChanged(val value: Boolean) : RedCodeEvent()
    data class OnAggressiveBehaviorChanged(val value: Boolean) : RedCodeEvent()
    data class OnPregnancyHeavyBleedingChanged(val value: Boolean) : RedCodeEvent()
    data class OnSevereAbdominalPainChanged(val value: Boolean) : RedCodeEvent()
    data class OnSeizuresChanged(val value: Boolean) : RedCodeEvent()
    data class OnAlteredMentalStatusChanged(val value: Boolean) : RedCodeEvent()
    data class OnSevereHeadacheChanged(val value: Boolean) : RedCodeEvent()
    data class OnVisualChangesChanged(val value: Boolean) : RedCodeEvent()
    data class OnSbpHighDpbHighChanged(val value: Boolean) : RedCodeEvent()
    data class OnTraumaChanged(val value: Boolean) : RedCodeEvent()
    data class OnActiveLaborChanged(val value: Boolean) : RedCodeEvent()
}