package com.unimib.oases.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VisitVitalSign (
    val visitId: String,
    val vitalSignName: String,
    val timestamp: String = "",
    val value: String = ""
)