package com.unimib.oases.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Patient(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var age: Int,
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