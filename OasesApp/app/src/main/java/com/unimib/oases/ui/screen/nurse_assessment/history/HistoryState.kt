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
    val mode: PmhMode = PmhMode.View(
        freeTextDiseases = emptyList(),
        selectionDiseases = emptyList()
    ),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    // 2. This computed property can now get the data directly from the mode.
    val isPastMedicalHistoryPresent: Boolean
        get() = mode is PmhMode.View && mode.isPastMedicalHistoryPresent

    val isSaveable: Boolean
        get() = mode is PmhMode.Edit && mode.hasAnyBeenFilledIn
}

// 3. Define the sealed interface for the different modes.
sealed interface PmhMode {
    /**
     * Represents the view-only state, holding the final displayed data.
     */
    data class View(
        val freeTextDiseases: List<PatientDiseaseState>,
        val selectionDiseases: List<PatientDiseaseState>
    ) : PmhMode {
        val diseases: List<PatientDiseaseState>
            get() = freeTextDiseases + selectionDiseases

        val isPastMedicalHistoryPresent: Boolean
            get() = diseases.any { it.isDiagnosed != null || it.freeTextValue.isNotBlank() }
    }

    /**
     * Represents the editing state, holding the data currently being edited in the form.
     */
    data class Edit(
        val originalFreeTextDiseases: List<PatientDiseaseState>,
        val originalSelectionDiseases: List<PatientDiseaseState>,

        val editingFreeTextDiseases: List<PatientDiseaseState> = originalFreeTextDiseases,
        val editingSelectionDiseases: List<PatientDiseaseState> = originalSelectionDiseases
    ) : PmhMode {
        val editingDiseases: List<PatientDiseaseState>
            get() = editingFreeTextDiseases + editingSelectionDiseases

        val areAllSetToNo: Boolean
            get() = editingSelectionDiseases.all{ it.isDiagnosed == false}

        val hasAnyBeenFilledIn: Boolean
            get() = editingDiseases.any { it.isDiagnosed != null || it.freeTextValue.isNotBlank() }
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