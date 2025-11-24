package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.screen.nurse_assessment.demographics.Sex
import com.unimib.oases.util.DateTimeFormatter

fun PatientEntity.toDomain(): Patient {
    val ageInMonths = DateTimeFormatter.calculateAgeInMonths(birthDate)
    return Patient(
        id = id,
        publicId = publicId,
        name = name,
        birthDate = birthDate,
        ageInMonths = ageInMonths ?: 0,
        sex = Sex.valueOf(sex),
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        image = image
    )
}

fun Patient.toEntity(): PatientEntity {
    return PatientEntity(
        id = id,
        publicId = publicId,
        name = name,
        birthDate = birthDate,
        sex = sex.name,
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        image = image
    )
}
