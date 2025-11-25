package com.unimib.oases.ui.screen.homepage

import com.unimib.oases.domain.model.PatientAndVisitIds

sealed class HomeScreenEvent {

    data class PatientItemClicked(val ids: PatientAndVisitIds) : HomeScreenEvent()
    object AddButtonClicked: HomeScreenEvent()

    object ToastShown: HomeScreenEvent()
}