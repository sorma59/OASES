package com.unimib.oases.ui.screen.nurse_assessment.patient_registration

import com.unimib.oases.data.local.model.PatientStatus
import com.unimib.oases.domain.model.Patient

data class PatientInfoState(
    val patient: Patient = Patient(
        name = "",
        birthDate = "",
        ageInMonths = 0 * 12,
        sex = Sex.UNSPECIFIED.displayName,
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
    val birthDateError: String?= null,
    val sexError: String?= null,
    val edited: Boolean = false,
    val error: String?= null,
)