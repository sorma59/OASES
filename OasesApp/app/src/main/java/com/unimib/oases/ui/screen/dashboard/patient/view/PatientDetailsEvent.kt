package com.unimib.oases.ui.screen.dashboard.patient.view

sealed class PatientDetailsEvent {
    data object OnRetry: PatientDetailsEvent()
}