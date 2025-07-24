package com.unimib.oases.ui.screen.homepage

import com.unimib.oases.domain.model.Patient

sealed class HomeScreenEvent {
    data class Share(val patient: Patient): HomeScreenEvent()
    data class Delete(val patient: Patient): HomeScreenEvent()

    object ToastShown: HomeScreenEvent()
}