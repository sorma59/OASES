package com.unimib.oases.domain.model

import com.unimib.oases.data.mapper.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Visit(
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    val triageCode: TriageCode = TriageCode.GREEN,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate = LocalDate.now(),
    val description: String = ""
){
    val status: VisitStatus
        get() = if (date.isBefore(LocalDate.now())) VisitStatus.CLOSED else VisitStatus.OPEN
}

enum class TriageCode{
    RED,
    YELLOW,
    GREEN;

    companion object {
        fun fromTriageCodeName(name: String): TriageCode =
            entries.first { it.name == name }
    }
}

enum class VisitStatus{
    OPEN,
    CLOSED
}