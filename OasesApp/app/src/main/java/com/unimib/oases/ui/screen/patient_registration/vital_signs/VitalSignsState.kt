package com.unimib.oases.ui.screen.patient_registration.vital_signs

data class VitalSignsState (
    var vitalSigns: List<PatientVitalSignState> = emptyList(),
    var isLoading: Boolean = false,
    var error: String? = null
)

data class PatientVitalSignState(
    var name: String,
    var acronym: String,
    var unit: String,
    var value: String = "",
    var error: String? = null
)