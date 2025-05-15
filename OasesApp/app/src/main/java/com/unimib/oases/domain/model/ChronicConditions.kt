package com.unimib.oases.domain.model

class ChronicConditions {
    val diabetes: Boolean = false
    val hypertension: Boolean = false
    val asthma: Boolean = false
    val arthritis: Boolean = false
    val depression: Boolean = false
}

data class ChronicCondition(
    val name: String,
    val diagnosisDate: String,
)