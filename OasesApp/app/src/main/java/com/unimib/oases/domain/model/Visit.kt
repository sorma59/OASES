package com.unimib.oases.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Visit(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val triageCode: String = TriageCode.GREEN.name,
    val date: String = "",
    val description: String = "",
    val status: String = VisitStatus.OPEN.name
)

enum class TriageCode()
{
    RED(),
    YELLOW(),
    GREEN()
}

enum class VisitStatus(){
    OPEN(),
    CLOSED()
}