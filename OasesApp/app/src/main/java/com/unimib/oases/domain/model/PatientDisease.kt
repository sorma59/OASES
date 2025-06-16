package com.unimib.oases.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PatientDisease(
    val patientId: String,
    val diseaseName: String,
    val diagnosisDate: String = "",
    val additionalInfo: String = ""
)