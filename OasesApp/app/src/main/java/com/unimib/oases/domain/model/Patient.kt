package com.unimib.oases.domain.model

import com.unimib.oases.ui.screen.nurse_assessment.demographics.Sex
import com.unimib.oases.util.PasswordUtils
import java.util.UUID

data class Patient(
    val id: String = UUID.randomUUID().toString(),
    val publicId: String = PasswordUtils.generateShortId(),
    val name: String,
    val birthDate: String,
    val ageInMonths: Int,
    val sex: Sex,
    val village: String,
    val parish: String,
    val subCounty: String,
    val district: String,
    val nextOfKin: String,
    val contact: String,
//    val status: PatientStatus,
//    val code: TriageCode,
//    val roomName: String,
//    val arrivalTime: LocalDateTime = LocalDateTime.now(),
    val image: ByteArray? = null
)

/**
 * Represents the different states a patient can be in during their hospital visit.
 */
enum class PatientStatus(val displayValue: String) {
    WAITING_FOR_TRIAGE("Waiting for triage"),
    WAITING_FOR_VISIT("Waiting for visit"),
    WAITING_FOR_TEST_RESULTS("Waiting for test results"),
    HOSPITALIZED("Hospitalized"),
    DISMISSED("Dismissed")
}