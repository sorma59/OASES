package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

sealed class VitalSignsEvent {
    data object AddButtonClicked: VitalSignsEvent()

    data object Retry: VitalSignsEvent()
}