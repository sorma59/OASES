package com.unimib.oases.data.model

import java.util.UUID

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
    var image: ByteArray? = null
)