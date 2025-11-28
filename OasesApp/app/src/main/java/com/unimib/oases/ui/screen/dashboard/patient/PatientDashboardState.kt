package com.unimib.oases.ui.screen.dashboard.patient

import com.unimib.oases.domain.model.PatientWithVisitInfo

data class PatientDashboardState(
    val patientId: String,
    val visitId: String,
    val patientWithVisitInfo: PatientWithVisitInfo? = null,
    val actions: List<PatientDashboardAction> = PatientDashboardAction.entries,

    val showAlertDialog: Boolean = false,
    val toastMessage: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val deletionState: DeletionState = DeletionState()
)

data class DeletionState(
    val isLoading: Boolean = false,
    val error: String? = null
)