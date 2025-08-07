package com.unimib.oases.ui.screen.nurse_assessment.past_medical_history

import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.SexSpecificity

data class PastHistoryState (
    val diseases: List<PatientDiseaseState> = emptyList(),
    val sex: SexSpecificity = SexSpecificity.ALL,
    val age: AgeSpecificity = AgeSpecificity.ALL,
    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null,
){
    val hasBeenFilledIn: Boolean
        get() = diseases.any { it.isDiagnosed != null }
}

data class PatientDiseaseState(
    val disease: String,
    val isDiagnosed: Boolean? = null, // null means not answered
    val additionalInfo: String = "",
    val date: String = ""
)