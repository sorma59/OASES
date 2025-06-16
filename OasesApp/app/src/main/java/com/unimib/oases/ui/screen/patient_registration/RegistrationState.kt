package com.unimib.oases.ui.screen.patient_registration

import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoState
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryState
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsState

data class RegistrationState (
    val patientInfoState: PatientInfoState = PatientInfoState(),
    val pastHistoryState: PastHistoryState = PastHistoryState(),
    val vitalSignsState: VitalSignsState = VitalSignsState(),
    val triageCode: String = TriageCode.GREEN.name,
    val error : String? = null,
    // Other state properties
)