package com.unimib.oases.ui.screen.nurse_assessment.vital_signs

sealed class VitalSignsEvent {
    data class ValueChanged(val vitalSign: String, val value: String): VitalSignsEvent()

    object Retry: VitalSignsEvent()
    object Submit: VitalSignsEvent()
}