package com.unimib.oases.ui.screen.homepage

sealed class HomeScreenEvent {

    data class PatientItemClicked(val patientId: String) : HomeScreenEvent()
    object AddButtonClicked: HomeScreenEvent()

    object ToastShown: HomeScreenEvent()
}