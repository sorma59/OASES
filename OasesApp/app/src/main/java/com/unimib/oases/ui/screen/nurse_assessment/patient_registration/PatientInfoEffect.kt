package com.unimib.oases.ui.screen.nurse_assessment.patient_registration

sealed class PatientInfoEffect {
    object SendValidationResult : PatientInfoEffect()
    object SavePatientData : PatientInfoEffect()
    object ComputeAge: PatientInfoEffect()
    object ComputeBirthDate: PatientInfoEffect()
}