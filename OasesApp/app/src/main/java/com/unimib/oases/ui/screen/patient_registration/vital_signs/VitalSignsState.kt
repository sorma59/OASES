package com.unimib.oases.ui.screen.patient_registration.vital_signs

data class VitalSignsState (
    val vitalSigns: List<PatientVitalSignState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PatientVitalSignState(
    val name: String,
    val acronym: String,
    val unit: String,
    val value: String = "",
    val error: String? = null
)