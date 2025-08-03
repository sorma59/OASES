package com.unimib.oases.ui.screen.patient_registration.triage

sealed class TriageEvent {

    data class FieldToggled(val fieldId: String) : TriageEvent()

    object ToastShown: TriageEvent()
}