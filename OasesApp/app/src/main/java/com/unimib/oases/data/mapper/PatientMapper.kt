package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.domain.model.Patient

fun PatientEntity.toPatient(): Patient {
    return Patient(
        id = id,
        publicId = publicId,
        name = name,
        birthDate = birthDate,
        age = age,
        sex = sex,
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        status = status,
        image = image
    )
}

fun Patient.toEntity(): PatientEntity {
    return PatientEntity(
        id = id,
        publicId = publicId,
        name = name,
        birthDate = birthDate,
        age = age,
        sex = sex,
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        status = status,
        image = image
    )
}
