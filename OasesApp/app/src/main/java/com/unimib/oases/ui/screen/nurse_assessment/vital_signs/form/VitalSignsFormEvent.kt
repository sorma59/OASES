package com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form

sealed class VitalSignsFormEvent {

    data class ValueChanged(val vitalSign: String, val value: String): VitalSignsFormEvent()

    data object Save: VitalSignsFormEvent()

    data object Cancel: VitalSignsFormEvent()

    data object ReattemptSaving: VitalSignsFormEvent()

    data object Retry: VitalSignsFormEvent()

}