package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.relation.PatientWithVisitInfoEntity
import com.unimib.oases.domain.model.PatientWithVisitInfo

fun PatientWithVisitInfoEntity.toDomain(): PatientWithVisitInfo {
    return PatientWithVisitInfo(
        patient = patientEntity.toDomain(),
        visit = visitEntity.toDomain()
    )
}

fun PatientWithVisitInfo.toEntity(): PatientWithVisitInfoEntity {
    return PatientWithVisitInfoEntity(
        patientEntity = patient.toEntity(),
        visitEntity = visit.toEntity()
    )
}