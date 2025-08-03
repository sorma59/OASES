package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.domain.model.TriageCode.Companion.fromTriageCodeName
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.model.VisitStatus.Companion.fromVisitStatusName

fun Visit.toEntity(): VisitEntity {
    return VisitEntity(
        id = id,
        patientId = patientId,
        triageCode = triageCode.name,
        date = date,
        description = description,
        status = status.name
    )
}

fun VisitEntity.toDomain(): Visit {
    return Visit(
        id = id,
        patientId = patientId,
        triageCode = fromTriageCodeName(triageCode),
        date = date,
        description = description,
        status = fromVisitStatusName(status)
    )
}