package com.unimib.oases.ui.screen.patient_registration.past_medical_history

import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.SexSpecificity

data class PastHistoryState (
    var diseases: List<PatientDiseaseState> = emptyList(),
    var sex: SexSpecificity = SexSpecificity.ALL,
    var age: AgeSpecificity = AgeSpecificity.ALL,
    var isLoading: Boolean = false,
    var error: String? = null
)

data class PatientDiseaseState(
    var disease: String,
    var isChecked: Boolean = false,
    var additionalInfo: String = "",
    var date: String = ""
)