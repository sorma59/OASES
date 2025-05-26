package com.unimib.oases.ui.screen.patient_registration.past_medical_history

data class PastHistoryState (
    var diseases: List<PatientDiseaseState> = emptyList(),
    var isLoading: Boolean = false,
    var error: String? = null
)

data class PatientDiseaseState(
    var disease: String,
    var isChecked: Boolean = false,
    var additionalInfo: String = "",
    var date: String = ""
)