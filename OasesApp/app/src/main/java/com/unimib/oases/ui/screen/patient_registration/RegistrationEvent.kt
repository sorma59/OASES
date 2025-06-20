package com.unimib.oases.ui.screen.patient_registration

import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoState
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryState
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsState

sealed class RegistrationEvent {

    data class PatientSubmitted(val patientInfoState: PatientInfoState) : RegistrationEvent()
    data class PastMedicalHistoryNext(val pastHistoryState: PastHistoryState) : RegistrationEvent()
    data class VitalSignsSubmitted(val vitalSignsState: VitalSignsState) : RegistrationEvent()
    data class TriageCodeSelected(val triageCode: String) : RegistrationEvent()
    // Other next events

    data class Submit(val reevaluateTriageCode: Boolean) : RegistrationEvent()
}