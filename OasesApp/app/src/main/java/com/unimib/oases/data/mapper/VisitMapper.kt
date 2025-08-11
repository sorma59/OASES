package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.domain.model.TriageCode.Companion.fromTriageCodeName
import com.unimib.oases.domain.model.Visit
import java.time.LocalDate

fun Visit.toEntity(): VisitEntity {
    return VisitEntity(
        id = id,
        patientId = patientId,
        triageCode = triageCode.name,
        date = date.toString(),
        description = description
    )
}

fun VisitEntity.toDomain(): Visit {
    return Visit(
        id = id,
        patientId = patientId,
        triageCode = fromTriageCodeName(triageCode),
        date = LocalDate.parse(date),
        description = description
    )
}