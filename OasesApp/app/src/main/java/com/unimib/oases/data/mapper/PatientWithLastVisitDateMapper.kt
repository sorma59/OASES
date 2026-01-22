package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.relation.PatientWithLastVisitDateEntity
import com.unimib.oases.domain.model.PatientWithLastVisitDate

fun PatientWithLastVisitDateEntity.toDomain(): PatientWithLastVisitDate {
    return PatientWithLastVisitDate(
        patient = patientEntity.toDomain(),
        lastVisitDate = lastVisitDate
    )
}

fun PatientWithLastVisitDate.toEntity(): PatientWithLastVisitDateEntity {
    return PatientWithLastVisitDateEntity(
        patientEntity = patient.toEntity(),
        lastVisitDate = lastVisitDate
    )
}