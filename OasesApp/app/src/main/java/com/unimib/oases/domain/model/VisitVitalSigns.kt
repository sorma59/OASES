package com.unimib.oases.domain.model

data class VisitVitalSigns (
    val visitId: String,
    val vitalSingsName: String,
    val timestamp: String = "",
    val value: Double
)