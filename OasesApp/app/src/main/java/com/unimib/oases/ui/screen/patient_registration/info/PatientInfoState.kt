package com.unimib.oases.ui.screen.patient_registration.info

import com.unimib.oases.domain.model.Patient

data class PatientInfoState(
    val patient: Patient = Patient(
        name = "",
        age = 0,
        sex = Sex.Unspecified.displayName,
        village = "",
        parish = "",
        subCounty = "",
        district = "",
        nextOfKin = "",
        contact = "",
        status = "",
        image = null
    ),
    val isLoading: Boolean = true,
    val nameError: String?= null,
    val ageError: String?= null,
    val error: String?= null,
)