package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

import com.unimib.oases.ui.screen.homepage.HomeScreenEvent

sealed class VitalSignsEvent {
    data class ValueChanged(val vitalSign: String, val value: String): VitalSignsEvent()

    data object Save: VitalSignsEvent()

    object AddButtonClicked: VitalSignsEvent()

    data object Retry: VitalSignsEvent()
}