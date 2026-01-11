package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode

data class TriageData(
    val selectedReds: Set<String> = emptySet(),
    val selectedYellows: Set<String> = emptySet(),
    val triageCode: TriageCode,
    val selectedRoom: Room? = null
)

/**
 * Holds all state related to the editing flow, including tab management,
 * button text logic, and the calculated triage code.
 */
data class EditingState(
    val triageConfig: TriageConfig,
    val triageData: TriageData = TriageData(triageCode = TriageCode.GREEN),
    val roomsState: RoomsState = RoomsState(),
    val tabStack: List<TriageTab> = listOf(TriageTab.REDS),
) {
    val currentTab: TriageTab
        get() = tabStack.last()

    val nextButtonText: String
        get() = if (nextTab() == null) "Save" else "Next"

    val cancelButtonText: String
        get() = if (tabStack.size == 1) "Cancel" else "Back"

    fun nextTab(): TriageTab? {
        return when (currentTab) {
            TriageTab.REDS -> {
                if (triageData.triageCode == TriageCode.RED) TriageTab.ROOM else TriageTab.YELLOWS
            }
            TriageTab.YELLOWS -> {
                if (triageData.triageCode == TriageCode.YELLOW) TriageTab.ROOM else TriageTab.VITAL_SIGNS
            }
            TriageTab.VITAL_SIGNS -> TriageTab.ROOM
            TriageTab.ROOM -> null // End of the editing flow
        }
    }
}

/**
 * Holds the state related to room selection.
 */
data class RoomsState (
    val rooms: List<Room> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

data class TriageState(
    // --- Core Identifiers (Always present) ---
    val patientId: String,
    val visitId: String,
    val uiMode: PatientRegistrationScreensUiMode,
    // --- High-Level UI State ---
    val isLoading: Boolean = true, // For initial screen data load
    val error: String? = null,
    val toastMessage: String? = null,
    val showAlertDialog: Boolean = false,
    // --- Domain Data ---
    val patient: Patient? = null,
    val visit: Visit? = null,
    val storedData: TriageData? = null,

    // --- Sub-States for Different UI Concerns ---
    // This is nullable: it only exists when the UI mode is Wizard.
    val editingState: EditingState? = null,

    val savingError: String? = null
)

enum class TriageTab(val title: String){
    REDS("Red code"),
    YELLOWS("Yellow code"),
    VITAL_SIGNS("Vital signs"),
    ROOM("Room selection")
}