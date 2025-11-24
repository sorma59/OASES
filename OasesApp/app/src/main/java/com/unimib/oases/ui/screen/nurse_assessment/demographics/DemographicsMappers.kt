package com.unimib.oases.ui.screen.nurse_assessment.demographics

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.screen.nurse_assessment.demographics.SexOption.Companion.fromSex

fun PatientData.toModel(): Patient {
    return if (id != null)
        Patient(
            id = id,
            name = name,
            birthDate = birthDate,
            ageInMonths = ageInMonths,
            sex = sexOption.sex!!,
            village = village,
            parish = parish,
            subCounty = subCounty,
            district = district,
            nextOfKin = nextOfKin,
            contact = contact
        )
    else
        Patient(
            name = name,
            birthDate = birthDate,
            ageInMonths = ageInMonths,
            sex = sexOption.sex!!,
            village = village,
            parish = parish,
            subCounty = subCounty,
            district = district,
            nextOfKin = nextOfKin,
            contact = contact
        )
}

fun Patient.toState(): PatientData {
    return PatientData(
        id = id,
        name = name,
        birthDate = birthDate,
        ageInMonths = ageInMonths,
        sexOption = fromSex(sex),
        village = village,
        parish = parish,
        subCounty = subCounty,
        district = district,
        nextOfKin = nextOfKin,
        contact = contact
    )
}