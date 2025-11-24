package com.unimib.oases.ui.screen.nurse_assessment

sealed class RegistrationEvent {
    data class PatientAndVisitCreated(val patientId: String, val visitId: String): RegistrationEvent()
    data object StepCompleted: RegistrationEvent()
    data object Retry: RegistrationEvent()
}