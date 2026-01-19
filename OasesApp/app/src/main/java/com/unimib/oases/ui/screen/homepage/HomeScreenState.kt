package com.unimib.oases.ui.screen.homepage

import com.unimib.oases.domain.model.PatientWithVisitInfo

data class HomeScreenState(
    val patientsWithVisitInfo: List<PatientWithVisitInfo> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null
)