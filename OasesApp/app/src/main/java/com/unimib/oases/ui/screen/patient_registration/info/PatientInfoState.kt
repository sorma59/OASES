package com.unimib.oases.ui.screen.patient_registration.info

import com.unimib.oases.data.local.model.PatientStatus
import com.unimib.oases.domain.model.Patient

data class PatientInfoState(
    val patient: Patient = Patient(
        name = "",
        birthDate = "",
        age = 0,
        sex = Sex.Unspecified.displayName,
        village = "",
        parish = "",
        subCounty = "",
        district = "",
        nextOfKin = "",
        contact = "",
        status = PatientStatus.WAITING_FOR_TRIAGE.displayValue,
        image = null
    ),
    val isLoading: Boolean = true,
    val nameError: String?= null,
    val ageError: String?= null,
    val error: String?= null,
)