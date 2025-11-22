package com.unimib.oases.ui.screen.nurse_assessment.triage

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode

data class TriageData(
    val selectedReds: Set<String> = emptySet(),
    val selectedYellows: Set<String> = emptySet(),
    val triageCode: TriageCode = TriageCode.GREEN,
    val selectedRoom: Room? = null
)

/**
 * Holds all state related to the editing flow, including tab management,
 * button text logic, and the calculated triage code.
 */
data class EditingState(
    val triageConfig: TriageConfig,
    val triageData: TriageData = TriageData(),
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

/**
 * Holds the state related to the save operation.
 */
data class SavingState(
    val isLoading: Boolean = false,
    val error: String? = null
)

data class TriageState(
    // --- Core Identifiers (Always present) ---
    val patientId: String,
    val visitId: String?,
    val uiMode: PatientRegistrationScreensUiMode,
    // --- High-Level UI State ---
    val isLoading: Boolean = true, // For initial screen data load
    val error: String? = null,
    val toastMessage: String? = null,
    val showAlertDialog: Boolean = false,
    // --- Domain Data ---
    val patient: Patient? = null,
    val visit: Visit? = null,
    val storedData: TriageData = TriageData(),

    // --- Sub-States for Different UI Concerns ---
    // This is nullable: it only exists when the UI mode is Wizard.
    val editingState: EditingState? = null,

    val savingState: SavingState = SavingState()
)

//data class TriageState(
//
//    val patientId: String,
//
//    val visitId: String?,
//
//    val initialUiMode: PatientRegistrationScreensUiMode, // Initial, used to know where to navigate back to
//    val uiMode: PatientRegistrationScreensUiMode = initialUiMode, // actual SSOT, change this
//
//    val patient: Patient? = null,
//
//    val visit: Visit? = null,
//
//    val currentStep: Int = 0,
//
//    val triageConfig: TriageConfig? = null,
//
//    val selectedReds: Set<String> = emptySet(),
//    val selectedYellows: Set<String> = emptySet(),
//
//    val triageCode: TriageCode = TriageCode.GREEN,
//
//    val roomsState: RoomsState = RoomsState(),
//
//    val tabStack: List<TriageTab> = listOf(TriageTab.REDS),
//
//    val savingState: SavingState = SavingState(),
//
//    val showAlertDialog: Boolean = false,
//
//    val editingState: EditingState = EditingState(
//        selectedReds,
//        selectedYellows,
//        roomsState.selectedRoom
//    ),
//
//    val isLoading: Boolean = false,
//    val error: String? = null,
//    val toastMessage: String? = null,
//    val loaded: Boolean = true
//) {
//    val nextButtonText: String
//        get() = if (nextTab() == null) "Save" else "Next"
//
//    val cancelButtonText: String
//        get() = if (tabStack.size == 1) "Cancel" else "Back"
//
//    val currentTab: TriageTab
//        get() = tabStack[tabStack.lastIndex]
//
//    fun nextTab(): TriageTab? {
//        return when (currentTab) {
//            TriageTab.REDS -> {
//                if (triageCode == TriageCode.RED)
//                    TriageTab.ROOM
//                else
//                    TriageTab.YELLOWS
//            }
//            TriageTab.YELLOWS -> {
//                if (triageCode == TriageCode.YELLOW)
//                    TriageTab.ROOM
//                else
//                    TriageTab.VITAL_SIGNS
//            }
//            TriageTab.VITAL_SIGNS -> TriageTab.ROOM
//            TriageTab.ROOM -> null // null since this is the last tab
//        }
//    }
//}
//
//data class RoomsState (
//    val rooms: List<Room> = emptyList(),
//    val selectedRoom: Room? = null,
//    val isLoading: Boolean = false,
//    val error: String? = null
//)
//
enum class TriageTab(val title: String){
    REDS("Red code"),
    YELLOWS("Yellow code"),
    VITAL_SIGNS("Vital signs"),
    ROOM("Room selection")
}
//
//data class SavingState(
//    val isLoading: Boolean = false,
//    val error: String? = null
//)
//
//data class EditingState(
//    val selectedReds: Set<String>,
//    val selectedYellows: Set<String>,
//    val selectedRoom: Room?
//)