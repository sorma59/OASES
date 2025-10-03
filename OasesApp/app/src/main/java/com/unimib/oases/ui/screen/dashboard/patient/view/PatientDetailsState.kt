package com.unimib.oases.ui.screen.dashboard.patient.view

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.Patient

data class PatientDetailsState(
    val patientId: String,
    val patient: Patient? = null,

    val mainComplaintsSummaries: List<ComplaintSummary> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null
)