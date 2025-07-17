package com.unimib.oases.ui.screen.patient_registration.past_medical_history

import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.SexSpecificity

data class PastHistoryState (
    var diseases: List<PatientDiseaseState> = emptyList(),
    var sex: SexSpecificity = SexSpecificity.ALL,
    var age: AgeSpecificity = AgeSpecificity.ALL,
    var isLoading: Boolean = false,
    var error: String? = null,
    var toastMessage: String? = null,
){
    val hasBeenFilledIn: Boolean
        get() = diseases.any { it.isDiagnosed != null }
}

data class PatientDiseaseState(
    var disease: String,
    var isDiagnosed: Boolean? = null, // null means not answered
    var additionalInfo: String = "",
    var date: String = ""
)