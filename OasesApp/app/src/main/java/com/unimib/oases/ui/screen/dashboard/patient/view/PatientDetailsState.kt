package com.unimib.oases.ui.screen.dashboard.patient.view

import com.unimib.oases.domain.model.Patient

data class PatientDetailsState(
    val receivedId: String,
    val patient: Patient? = null,

    val isLoading: Boolean = false,
    val error: String? = null
)