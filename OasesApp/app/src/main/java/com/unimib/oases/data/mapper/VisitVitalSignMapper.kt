package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.VisitVitalSignEntity
import com.unimib.oases.domain.model.VisitVitalSign

fun VisitVitalSign.toEntity(): VisitVitalSignEntity {
    return VisitVitalSignEntity(
        visitId = visitId,
        timestamp = timestamp,
        vitalSignName = vitalSignName,
        value = value
    )
}

fun VisitVitalSignEntity.toVisitVitalSign(): VisitVitalSign {
    return VisitVitalSign(
        visitId = visitId,
        timestamp = timestamp,
        vitalSignName = vitalSignName,
        value = value
    )
}