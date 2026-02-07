package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.ui.graphics.Color
import com.unimib.oases.domain.model.DiseaseEntryType
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.PmhGroup

data class HistoryState(
    val patientId: String,
    val selectedTab: HistoryScreenTab,
    val tabs: List<HistoryScreenTab>,
    val pastMedicalHistoryState: PastMedicalHistoryState = PastMedicalHistoryState(),
    val pastVisitsState: PastVisitsState = PastVisitsState()
)

data class PastMedicalHistoryState(
    // 1. The 'mode' property replaces 'isEditing' and 'editingDiseases'.
    //    It defaults to the View mode with an empty list.
    val mode: PmhMode = PmhMode.View(diseases = emptyList()),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    // 2. This computed property can now get the data directly from the mode.
    val isPastMedicalHistoryPresent: Boolean
        get() = mode is PmhMode.View && mode.diseases.mapNotNull{it.isDiagnosed}.isNotEmpty()

    val isSaveable: Boolean
        get() = mode is PmhMode.Edit && mode.hasAnyBeenFilledIn
}

// 3. Define the sealed interface for the different modes.
sealed interface PmhMode {
    /**
     * Represents the view-only state, holding the final displayed data.
     */
    data class View(val diseases: List<PatientDiseaseState>) : PmhMode

    /**
     * Represents the editing state, holding the data currently being edited in the form.
     */
    data class Edit(
        val originalDiseases: List<PatientDiseaseState>,
        val editingDiseases: List<PatientDiseaseState> = originalDiseases
    ) : PmhMode {
        val areAllSetToNo: Boolean
            get() = editingDiseases.all{ it.isDiagnosed == false}

        val hasAnyBeenFilledIn: Boolean
            get() = editingDiseases.any { it.isDiagnosed != null }
    }
}

data class PatientDiseaseState(
    val disease: String,
    val group: PmhGroup,
    val entryType: DiseaseEntryType,
    val isDiagnosed: Boolean? = null, // null means not answered
    val additionalInfo: String = "",
    val date: String = "",
    val freeTextValue: String = ""
)

data class PastVisitsState(
    val visits: List<VisitState> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

data class VisitState(
    val visitId: String,
    val date: String,
    val triageCode: Color,
    val status: PatientStatus
)

enum class HistoryScreenTab(val title: String) {
    PAST_MEDICAL_HISTORY("Chronic diseases"),
    PAST_VISITS("Past visits")
}