package com.unimib.oases.ui.screen.dashboard.patient

sealed class PatientDashboardEvent {
    data class ActionButtonClicked(val action: PatientDashboardAction): PatientDashboardEvent()

    data object PatientDeletionConfirmed: PatientDashboardEvent()

    data object PatientDeletionCancelled: PatientDashboardEvent()
    data object OnBack: PatientDashboardEvent()
    data object Refresh: PatientDashboardEvent()
}