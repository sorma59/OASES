package com.unimib.oases.ui.screen.homepage

sealed class HomeScreenUiEvent {
    data object LoadPatients : HomeScreenUiEvent()
    data class PatientClicked(val patientId: String) : HomeScreenUiEvent()
    data object Retry : HomeScreenUiEvent()
    data object Refresh : HomeScreenUiEvent()
    data object AddPatientClicked : HomeScreenUiEvent()
    // Add more events like SearchUpdated, FilterChanged, etc.
}