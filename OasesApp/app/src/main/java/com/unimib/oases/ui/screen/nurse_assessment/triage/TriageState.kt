package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.symptom.PatientCategory

data class TriageState(

    // Birthdate & Vital Signs
    val ageInMonths: Int? = null,
    val sbp: Int? = null,
    val dbp: Int? = null,
    val hr: Int? = null,
    val rr: Int? = null,
    val spo2: Int? = null,
    val temp: Double? = null,

    val triageConfig: TriageConfig? = null,

    val patientCategory: PatientCategory = PatientCategory.ADULT,

    val selectedReds: Set<String> = emptySet(),
    val selectedYellows: Set<String> = emptySet(),

    val triageCode: TriageCode = TriageCode.GREEN,

    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null,
    val loaded: Boolean = true
) {

    val isRedCode: Boolean
        get() = triageCode == TriageCode.RED
}