package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.VisitEntity
import com.unimib.oases.domain.model.Visit

fun Visit.toEntity(): VisitEntity {
    return VisitEntity(
        id = id,
        patientId = patientId,
        triageCode = triageCode,
        date = date,
        description = description,
        status = status
    )
}

fun VisitEntity.toDomain(): Visit {
    return Visit(
        id = id,
        patientId = patientId,
        triageCode = triageCode,
        date = date,
        description = description,
        status = status
    )
}