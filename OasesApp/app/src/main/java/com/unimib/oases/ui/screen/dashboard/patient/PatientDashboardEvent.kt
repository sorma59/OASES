package com.unimib.oases.ui.screen.dashboard.patient

sealed class PatientDashboardEvent {
    data class ActionButtonClicked(val button: PatientDashboardButton): PatientDashboardEvent()
    data object PatientItemClicked: PatientDashboardEvent()

    data object PatientDeletionConfirmed: PatientDashboardEvent()
    data object OnBack: PatientDashboardEvent()
}