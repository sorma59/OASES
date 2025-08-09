package com.unimib.oases.ui.screen.nurse_assessment

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningState
import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PastHistoryState
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoState
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsState

data class RegistrationState (
    val patientInfoState: PatientInfoState = PatientInfoState(),
    val pastHistoryState: PastHistoryState = PastHistoryState(),
    val vitalSignsState: VitalSignsState = VitalSignsState(),
    val visitHistoryState: VisitHistoryState = VisitHistoryState(),
    val triageState: TriageState = TriageState(),
    val malnutritionScreeningState: MalnutritionScreeningState = MalnutritionScreeningState(),
    val currentVisit: Visit? = null,
    val error : String? = null,
)