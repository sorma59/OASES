package com.unimib.oases.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TriageEvaluation(
    val visitId: String,

    val redSymptomIds: List<String>,

    val yellowSymptomIds: List<String>,
)