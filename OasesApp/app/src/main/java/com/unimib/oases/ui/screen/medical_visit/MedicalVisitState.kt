package com.unimib.oases.ui.screen.medical_visit

import com.unimib.oases.domain.model.ComplaintSummary

data class MedicalVisitState(
    val patientId: String,
    val visitId: String,
    val complaintSummaries: Map<String, ComplaintSummary> = emptyMap(),

    val isLoading: Boolean = false,
    val error: String? = null,
)