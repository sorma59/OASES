package com.unimib.oases.ui.screen.nurse_assessment

data class RegistrationState (
    val patientId: String? = null,
    val visitId: String? = null,
    val currentTab: Tab = Tab.START_WITH_DEMOGRAPHICS_DECISION,
    val wereVitalSignsTaken: Boolean = false,
    val error : String? = null
){

    fun nextTab(): Tab? {
        return when (currentTab) {
            Tab.START_WITH_DEMOGRAPHICS_DECISION -> Tab.CONTINUE_TO_TRIAGE
            Tab.CONTINUE_TO_TRIAGE -> if (wereVitalSignsTaken) Tab.CONTINUE_TO_MALNUTRITION_SCREENING else Tab.CONTINUE_TO_VITAL_SIGNS
            Tab.CONTINUE_TO_VITAL_SIGNS -> Tab.CONTINUE_TO_MALNUTRITION_SCREENING
            Tab.CONTINUE_TO_MALNUTRITION_SCREENING -> Tab.SUBMIT_ALL
            Tab.SUBMIT_ALL -> null
        }
    }
}

sealed interface PatientRegistrationScreensUiMode {
    object Wizard : PatientRegistrationScreensUiMode
    data class Standalone(val isEditing: Boolean = false) : PatientRegistrationScreensUiMode
}