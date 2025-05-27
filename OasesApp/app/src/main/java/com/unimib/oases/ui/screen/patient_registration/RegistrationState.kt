package com.unimib.oases.ui.screen.patient_registration

import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryState

data class RegistrationState (
    val pastHistoryState: PastHistoryState = PastHistoryState(),
    // Other state properties
)