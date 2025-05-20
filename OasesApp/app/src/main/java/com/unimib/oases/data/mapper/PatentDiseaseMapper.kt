package com.unimib.oases.data.mapper

import com.unimib.oases.data.model.PatientDiseaseEntity
import com.unimib.oases.domain.model.PatientDisease

fun PatientDiseaseEntity.toPatientDisease(): PatientDisease {
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