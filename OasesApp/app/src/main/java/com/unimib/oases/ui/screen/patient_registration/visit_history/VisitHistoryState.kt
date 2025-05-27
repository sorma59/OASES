package com.unimib.oases.ui.screen.patient_registration.visit_history

import com.unimib.oases.domain.model.Visit

data class VisitHistoryState(
    val visits: List<Visit> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)