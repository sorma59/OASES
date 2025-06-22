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
    var name: String,
    var birthDate: String,
    var ageInMonths: Int,
    var sex: String,
    var village: String,
    var parish: String,
    var subCounty: String,
    var district: String,
    var nextOfKin: String,
    var contact: String,
    var status: String,
    var image: ByteArray? = null
) : Parcelable