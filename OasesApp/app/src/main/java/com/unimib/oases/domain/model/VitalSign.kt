package com.unimib.oases.domain.model

data class VitalSign(
    val name: String,
    val acronym: String = "",
    val unit: String = ""
)
