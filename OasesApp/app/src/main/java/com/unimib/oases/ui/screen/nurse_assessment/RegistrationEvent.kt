package com.unimib.oases.ui.screen.nurse_assessment

sealed class RegistrationEvent {

    data object PatientSubmitted : RegistrationEvent()
    data object VitalSignsSubmitted : RegistrationEvent()

    data object Submit : RegistrationEvent()
}