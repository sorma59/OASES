package com.unimib.oases.ui.screen.dashboard.patient

sealed class PatientDashboardEvent {
    data class ActionButtonClicked(val button: PatientDashboardButton): PatientDashboardEvent()
}