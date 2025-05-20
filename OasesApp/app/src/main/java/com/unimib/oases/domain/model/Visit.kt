package com.unimib.oases.domain.model

import java.util.UUID

data class Visit(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val triageCode: String = TriageCode.GREEN.name,
    val date: String = "",
    val description: String = "",
)

enum class TriageCode(val code: String)
{
    RED("RED"),
    YELLOW("YELLOW"),
    GREEN("GREEN")
}