package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.relation.PatientWithVisitInfoEntity
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.model.TriageCode
import java.time.LocalTime

fun PatientWithVisitInfoEntity.toDomain(): PatientWithVisitInfo {
    return PatientWithVisitInfo(
        patient = patientEntity.toDomain(),
        status = PatientStatus.valueOf(status),
        code = TriageCode.valueOf(code),
        room = room,
        arrivalTime = LocalTime.parse(arrivalTime),
    )
}

fun PatientWithVisitInfo.toEntity(): PatientWithVisitInfoEntity {
    return PatientWithVisitInfoEntity(
        patientEntity = patient.toEntity(),
        status = status.name,
        code = code.name,
        room = room,
        arrivalTime = arrivalTime.toString()
    )
}