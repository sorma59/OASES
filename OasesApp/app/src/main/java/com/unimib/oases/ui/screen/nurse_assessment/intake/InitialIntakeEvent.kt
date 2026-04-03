package com.unimib.oases.ui.screen.nurse_assessment.intake

import com.unimib.oases.domain.model.Patient

sealed class InitialIntakeEvent {
    data object NewButtonClicked: InitialIntakeEvent()
    data object ReturnButtonClicked: InitialIntakeEvent()
    data class PatientClicked(val patient: Patient): InitialIntakeEvent()
}