package com.unimib.oases.ui.screen.past_visit

import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.screen.nurse_assessment.demographics.PatientData
import com.unimib.oases.ui.screen.nurse_assessment.history.PatientDiseaseState
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningData
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageData

data class PastVisitSummaryState(
    val visitId: String,
    val patientId: String,

    val visit: Visit? = null,

    val patientData: PatientData = PatientData(patientId),
    val isPatientDataLoading: Boolean = false,
    val patientError: String? = null,

    val triageData: TriageData = TriageData(triageCode = TriageCode.RED),
    val isTriageDataLoading: Boolean = false,
    val triageError: String? = null,

    val malnutritionData: MalnutritionScreeningData? = null,
    val isMalnutritionDataLoading: Boolean = false,
    val malnutritionError: String? = null,

    val chronicDiseasesData: Pair<List<PatientDiseaseState>, List<PatientDiseaseState>> = emptyList<PatientDiseaseState>() to emptyList(),
    val isChronicDiseasesDataLoading: Boolean = false,
    val chronicDiseasesError: String? = null,

) {
    val isLoading: Boolean
        get() = isPatientDataLoading || isTriageDataLoading || isMalnutritionDataLoading || isChronicDiseasesDataLoading
}