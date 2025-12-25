package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.ui.graphics.Color

data class HistoryState(
    val patientId: String,
    val selectedTab: HistoryScreenTab,
    val tabs: List<HistoryScreenTab>,
    val pastMedicalHistoryState: PastMedicalHistoryState = PastMedicalHistoryState(),
    val pastVisitsState: PastVisitsState = PastVisitsState(),
    val toastMessage: String? = null
)

data class PastMedicalHistoryState(
    val isEditing: Boolean = false,
    val diseases: List<PatientDiseaseState> = emptyList(),
    val editingDiseases: List<PatientDiseaseState> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

data class EditingState(
    val diseases: List<PatientDiseaseState> = emptyList()
)

data class PatientDiseaseState(
    val disease: String,
    val isDiagnosed: Boolean? = null, // null means not answered
    val additionalInfo: String = "",
    val date: String = ""
)

data class PastVisitsState(
    val visits: List<VisitState> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

data class VisitState(
    val visitId: String,
    val date: String,
    val triageCode: Color
)

enum class HistoryScreenTab(val title: String) {
    PAST_MEDICAL_HISTORY("Chronic diseases"),
    PAST_VISITS("Past visits")
}