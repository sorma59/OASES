package com.unimib.oases.domain.model

import com.unimib.oases.data.mapper.serializer.LocalDateSerializer
import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.SupportiveTherapyText
import com.unimib.oases.domain.model.symptom.Symptom
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
    val description: String = "",
    val complaints: Set<ComplaintSummary> = emptySet()
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
@Serializable
data class ComplaintSummary(
    val complaintId: String,
    val symptoms: Set<Symptom>,
    val tests: Set<LabelledTest>,
    val immediateTreatments: Set<ImmediateTreatment>,
    val supportiveTherapies: Set<SupportiveTherapyText>,
    val additionalTests: String
)