package com.unimib.oases.domain.model

data class PatientDisease(
    val patientId: String,
    val diseaseName: String,
    val isDiagnosed: Boolean,
    val diagnosisDate: String = "",
    val additionalInfo: String = "",
    val freeTextValue: String = ""
)