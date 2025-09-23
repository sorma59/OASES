package com.unimib.oases.ui.screen.nurse_assessment.patient_registration

import com.unimib.oases.data.local.model.PatientStatus
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.TriageCode
import java.time.LocalDateTime

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
        code = "",
        room = "",
        arrivalTime = LocalDateTime.now(),
        status = PatientStatus.WAITING_FOR_TRIAGE.displayValue
    ),
    val isLoading: Boolean = false,
    val nameError: String?= null,
    val birthDateError: String?= null,
    val sexError: String?= null,
    val isEdited: Boolean = false,
    val isNew: Boolean = true,
    val error: String?= null
)