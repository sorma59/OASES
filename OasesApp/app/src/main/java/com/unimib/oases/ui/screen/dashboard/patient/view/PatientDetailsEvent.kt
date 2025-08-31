package com.unimib.oases.ui.screen.dashboard.patient.view

sealed class PatientDetailsEvent {
    data object OnBack: PatientDetailsEvent()
    data object OnNext: PatientDetailsEvent()
    data object OnEdit: PatientDetailsEvent()
    data object OnRetry: PatientDetailsEvent()
}