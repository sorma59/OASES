package com.unimib.oases.ui.screen.nurse_assessment

import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsState

sealed class RegistrationEvent {

    data class PatientSubmitted(val patientInfoState: PatientInfoState) : RegistrationEvent()
    data class VitalSignsSubmitted(val vitalSignsState: VitalSignsState) : RegistrationEvent()

    object Submit : RegistrationEvent()
}