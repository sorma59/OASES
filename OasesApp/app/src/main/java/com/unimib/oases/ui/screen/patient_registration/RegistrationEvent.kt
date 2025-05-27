package com.unimib.oases.ui.screen.patient_registration

import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoState
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryState

sealed class RegistrationEvent {

    data class PatientSubmitted(val patientInfoState: PatientInfoState) : RegistrationEvent()
    data class PastMedicalHistoryNext(val pastHistoryState: PastHistoryState) : RegistrationEvent()
    // Other next events

    object Submit : RegistrationEvent()
}