package com.unimib.oases.ui.screen.nurse_assessment.patient_registration

sealed class PatientInfoEffect {
    data object SendValidationResult : PatientInfoEffect()
    data object SavePatientData : PatientInfoEffect()
    data object ComputeAge: PatientInfoEffect()
    data object ComputeBirthDate: PatientInfoEffect()
    data object Retry: PatientInfoEffect()
}