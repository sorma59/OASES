package com.unimib.oases.ui.screen.nurse_assessment

sealed class RegistrationEvent {

    object PatientSubmitted : RegistrationEvent()
    object VitalSignsSubmitted : RegistrationEvent()

    object Submit : RegistrationEvent()
}