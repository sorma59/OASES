package com.unimib.oases.ui.screen.patient_registration

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoState
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryState
import com.unimib.oases.ui.screen.patient_registration.triage.TriageState
import com.unimib.oases.ui.screen.patient_registration.visit_history.VisitHistoryState
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsState

data class RegistrationState (
    val patientInfoState: PatientInfoState = PatientInfoState(),
    val pastHistoryState: PastHistoryState = PastHistoryState(),
    val vitalSignsState: VitalSignsState = VitalSignsState(),
    val visitHistoryState: VisitHistoryState = VisitHistoryState(),
    val triageState: TriageState = TriageState(),
    val currentVisit: Visit? = null,
    val error : String? = null,
    // Other state properties
)