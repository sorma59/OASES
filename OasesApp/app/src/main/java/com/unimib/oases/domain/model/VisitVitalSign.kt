package com.unimib.oases.domain.model

data class VisitVitalSign (
    val visitId: String,
    val vitalSignName: String,
    val timestamp: String,
    val value: Double
)