package com.unimib.oases.ui.screen.nurse_assessment

import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsState

sealed class RegistrationEvent {

    object PatientSubmitted : RegistrationEvent()
    data class VitalSignsSubmitted(val vitalSignsState: VitalSignsState) : RegistrationEvent()

    object Submit : RegistrationEvent()
}