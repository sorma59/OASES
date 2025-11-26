package com.unimib.oases.ui.screen.medical_visit.pmh

import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.model.SexSpecificity

data class PastHistoryState (
    val patientId: String,
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

fun PastHistoryState.toPatientDiseases(): List<PatientDisease> {
    val list = mutableListOf<PatientDisease>()
    diseases.forEach {
        if (it.isDiagnosed != null) {
            list.add(
                PatientDisease(
                    patientId = this.patientId,
                    diseaseName = it.disease,
                    isDiagnosed = it.isDiagnosed,
                    additionalInfo = it.additionalInfo,
                    diagnosisDate = it.date
                )
            )
        }
    }
    return list.toList()
}