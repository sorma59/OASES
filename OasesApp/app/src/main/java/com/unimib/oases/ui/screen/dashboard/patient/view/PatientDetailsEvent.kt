package com.unimib.oases.ui.screen.dashboard.patient.view

sealed class PatientDetailsEvent {
    data object OnRetryPatientDetails: PatientDetailsEvent()
    data object OnRetryCurrentVisitRelated: PatientDetailsEvent()
}