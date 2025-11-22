package com.unimib.oases.ui.screen.nurse_assessment

data class RegistrationState (
    val patientId: String? = null,
//    val vitalSignsState: VitalSignsState = VitalSignsState(),
//    val visitHistoryState: VisitHistoryState = VisitHistoryState(),
//    val triageState: TriageState = TriageState(),
//    val roomsState: RoomsState = RoomsState(),
//    val malnutritionScreeningState: MalnutritionScreeningState = MalnutritionScreeningState(),
    val visitId: String? = null,
    val currentStep: Int = 0,
    val error : String? = null,
){
    val tabs = arrayOf(
        Tab.START_WITH_DEMOGRAPHICS_DECISION,
//        Tab.DEMOGRAPHICS,
        Tab.CONTINUE_TO_TRIAGE,
//        Tab.VITAL_SIGNS,
//        Tab.RED_CODE,
//        Tab.YELLOW_CODE,
//        Tab.ROOM_SELECTION,
        Tab.CONTINUE_TO_MALNUTRITION_SCREENING,
//        Tab.PAST_MEDICAL_HISTORY,
//        Tab.MALNUTRITION_SCREENING,
//        Tab.HISTORY,
        Tab.SUBMIT_ALL
    )

    val currentTab: Tab
        get() = tabs[currentStep]
}

sealed interface PatientRegistrationScreensUiMode {
    object Wizard : PatientRegistrationScreensUiMode
    data class Standalone(val isEditing: Boolean = false) : PatientRegistrationScreensUiMode
}