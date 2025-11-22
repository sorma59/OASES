package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.PatientEntity
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.util.DateTimeFormatter
import java.time.LocalDateTime

fun PatientEntity.toDomain(): Patient {
    val ageInMonths = DateTimeFormatter().calculateAgeInMonths(birthDate)
    return Patient(
        id = id,
        publicId = publicId,
        name = name,
        birthDate = birthDate,
        ageInMonths = ageInMonths ?: 0,
        sex = sex,
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        status = PatientStatus.valueOf(status),
        code = TriageCode.valueOf(code),
        roomName = room,
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
        sex = sex,
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact,
        status = status.name,
        code = code.name,
        room = roomName,
        arrivalTime = arrivalTime.toString(),
        image = image
    )
}
