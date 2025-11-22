package com.unimib.oases.ui.screen.dashboard.patient

import com.unimib.oases.domain.model.Patient

data class PatientDashboardState(
    val patientId: String,
    val patient: Patient? = null,
    val actions: List<PatientDashboardAction> = PatientDashboardAction.entries,

    val toastMessage: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)