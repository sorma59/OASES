package com.unimib.oases.ui.screen.nurse_assessment.triage

sealed class TriageEvent {

    data class FieldToggled(val fieldId: String) : TriageEvent()

    object ToastShown: TriageEvent()
}