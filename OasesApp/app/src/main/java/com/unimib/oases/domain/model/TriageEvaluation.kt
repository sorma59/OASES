package com.unimib.oases.domain.model

data class TriageEvaluation(
    val visitId: String,

    val redSymptomIds: List<String>,

    val yellowSymptomIds: List<String>,
)