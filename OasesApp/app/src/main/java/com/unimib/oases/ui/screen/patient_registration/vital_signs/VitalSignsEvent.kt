package com.unimib.oases.ui.screen.patient_registration.vital_signs

sealed class VitalSignsEvent {
    data class ValueChanged(val vitalSign: String, val value: String): VitalSignsEvent()

    object Submit: VitalSignsEvent()
}