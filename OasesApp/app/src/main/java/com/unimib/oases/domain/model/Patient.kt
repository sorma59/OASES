package com.unimib.oases.domain.model

import android.os.Parcelable
import com.unimib.oases.data.mapper.serializer.LocalDateSerializer
import com.unimib.oases.data.mapper.serializer.LocalDateTimeSerializer
import com.unimib.oases.util.PasswordUtils
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Parcelize
@Serializable
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
    @Serializable(with = LocalDateTimeSerializer::class)
    val arrivalTime: LocalDateTime = LocalDateTime.now(),
    val image: ByteArray? = null
) : Parcelable