package com.unimib.oases.ui.screen.nurse_assessment

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningState
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoState
import com.unimib.oases.ui.screen.nurse_assessment.room_selection.RoomState
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsState

data class RegistrationState (
    val patientInfoState: PatientInfoState = PatientInfoState(),
    val vitalSignsState: VitalSignsState = VitalSignsState(),
    val visitHistoryState: VisitHistoryState = VisitHistoryState(),
    val triageState: TriageState = TriageState(),
    val roomState: RoomState = RoomState(),
    val malnutritionScreeningState: MalnutritionScreeningState = MalnutritionScreeningState(),
    val currentVisit: Visit? = null,
    val currentStep: Int = 0,
    val error : String? = null,
){
    val tabs = arrayOf(
        Tab.DEMOGRAPHICS,
        Tab.CONTINUE_TO_TRIAGE,
        Tab.VITAL_SIGNS,
        Tab.RED_CODE,
        Tab.YELLOW_CODE,
        Tab.ROOM_SELECTION,
        Tab.HISTORY,
//        Tab.PAST_MEDICAL_HISTORY,
        Tab.MALNUTRITION_SCREENING,
        Tab.SUBMIT_ALL
    )

    val currentTab: Tab
        get() = tabs[currentStep]
}