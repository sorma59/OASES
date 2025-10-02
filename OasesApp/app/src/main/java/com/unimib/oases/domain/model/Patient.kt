package com.unimib.oases.domain.model

import com.unimib.oases.util.PasswordUtils
import java.time.LocalDateTime
import java.util.UUID

data class Patient(
    val id: String = UUID.randomUUID().toString(),
    val publicId: String = PasswordUtils.generateShortId(),
    val name: String,
    val birthDate: String,
    val ageInMonths: Int,
    val sex: String,
    val village: String,
    val parish: String,
    val subCounty: String,
    val district: String,
    val nextOfKin: String,
    val contact: String,
    val status: String,
    val code: String,
    val room: String,
    val arrivalTime: LocalDateTime = LocalDateTime.now(),
    val image: ByteArray? = null
)