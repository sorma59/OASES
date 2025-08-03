package com.unimib.oases.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Visit(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val triageCode: TriageCode = TriageCode.GREEN,
    val date: String = "",
    val description: String = "",
    val status: VisitStatus = VisitStatus.OPEN
)

enum class TriageCode()
{
    RED(),
    YELLOW(),
    GREEN();

    companion object {
        fun fromTriageCodeName(name: String): TriageCode =
            entries.first { it.name == name }
    }
}

enum class VisitStatus(){
    OPEN(),
    CLOSED();

    companion object {
        fun fromVisitStatusName(name: String): VisitStatus =
            entries.first { it.name == name }
    }
}