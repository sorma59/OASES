package com.unimib.oases.ui.screen.nurse_assessment.intake

sealed class InitialIntakeEvent {
    data object NewButtonClicked: InitialIntakeEvent()
    data object ReturnButtonClicked: InitialIntakeEvent()
    data class PatientClicked(val patientId: String): InitialIntakeEvent()
}