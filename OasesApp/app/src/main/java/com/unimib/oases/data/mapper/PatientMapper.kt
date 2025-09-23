package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.Sex.Companion.fromDisplayName
import com.unimib.oases.util.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalDateTime

fun PatientEntity.toDomain(): Patient {
    val ageInMonths = DateTimeFormatter().calculateAgeInMonths(birthDate)
    return Patient(
        id = id,
        publicId = publicId,
        name = name,
        birthDate = birthDate,
        ageInMonths = ageInMonths ?: 0,
        sex = sex.displayName,
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        status = status,
        code = code,
        room = room,
        arrivalTime = LocalDateTime.parse(arrivalTime),
        image = image,

    )
}

fun Patient.toEntity(): PatientEntity {
    return PatientEntity(
        id = id,
        publicId = publicId,
        name = name,
        birthDate = birthDate,
        sex = fromDisplayName(sex),
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        status = status,
        code = code,
        room = room,
        arrivalTime = arrivalTime.toString(),
        image = image
    )
}
