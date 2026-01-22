package com.unimib.oases.domain.model

import com.unimib.oases.ui.screen.nurse_assessment.demographics.Sex
import java.time.LocalDate

data class PatientWithLastVisitDate(
    val patient: Patient,
    val lastVisitDate: LocalDate
) {
    val id: String
        get() = patient.id
    val publicId: String
        get() = patient.publicId
    val name: String
        get() = patient.name
    val birthDate: String
        get() = patient.birthDate
    val ageInMonths: Int
        get() = patient.ageInMonths
    val sex: Sex
        get() = patient.sex
    val village: String
        get() = patient.village
    val parish: String
        get() = patient.parish
    val subCounty: String
        get() = patient.subCounty
    val district: String
        get() = patient.district
    val nextOfKin: String
        get() = patient.nextOfKin
    val contact: String
        get() = patient.contact
}