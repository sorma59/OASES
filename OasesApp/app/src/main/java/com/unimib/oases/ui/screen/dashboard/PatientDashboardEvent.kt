package com.unimib.oases.ui.screen.dashboard

sealed class PatientDashboardEvent {
    data class ActionButtonClicked(val button: PatientDashboardButton): PatientDashboardEvent()
}