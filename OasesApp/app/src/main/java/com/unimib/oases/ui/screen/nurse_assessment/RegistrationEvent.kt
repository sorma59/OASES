package com.unimib.oases.ui.screen.nurse_assessment

sealed class RegistrationEvent {
    data class PatientCreated(val patientId: String): RegistrationEvent()
    data class VisitCreated(val visitId: String): RegistrationEvent()
    data object StepCompleted: RegistrationEvent()
    data object Retry: RegistrationEvent()
}