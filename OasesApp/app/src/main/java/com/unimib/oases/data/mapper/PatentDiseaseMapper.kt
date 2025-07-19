package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.PatientDiseaseEntity
import com.unimib.oases.domain.model.PatientDisease

fun PatientDiseaseEntity.toDomain(): PatientDisease {
    return PatientDisease(
        patientId = patientId,
        diseaseName = diseaseName,
        diagnosisDate = diagnosisDate,
        additionalInfo = additionalInfo
    )
}

fun PatientDisease.toEntity(): PatientDiseaseEntity {
    return PatientDiseaseEntity(
        patientId = patientId,
        diseaseName = diseaseName,
        diagnosisDate = diagnosisDate,
        additionalInfo = additionalInfo
    )
}