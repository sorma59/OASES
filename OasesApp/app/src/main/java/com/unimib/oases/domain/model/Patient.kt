package com.unimib.oases.domain.model

import android.os.Parcelable
import com.unimib.oases.util.PasswordUtils
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
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
    val image: ByteArray? = null
) : Parcelable