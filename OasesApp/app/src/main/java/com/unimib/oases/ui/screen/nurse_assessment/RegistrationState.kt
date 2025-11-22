package com.unimib.oases.ui.screen.nurse_assessment

data class RegistrationState (
    val patientId: String? = null,
    val visitId: String? = null,
    val currentStep: Int = 0,
    val error : String? = null,
){
    val tabs = arrayOf(
        Tab.START_WITH_DEMOGRAPHICS_DECISION,
        Tab.CONTINUE_TO_TRIAGE,
        Tab.CONTINUE_TO_MALNUTRITION_SCREENING,
        Tab.SUBMIT_ALL
    )

    val currentTab: Tab
        get() = tabs[currentStep]
}

sealed interface PatientRegistrationScreensUiMode {
    object Wizard : PatientRegistrationScreensUiMode
    data class Standalone(val isEditing: Boolean = false) : PatientRegistrationScreensUiMode
}