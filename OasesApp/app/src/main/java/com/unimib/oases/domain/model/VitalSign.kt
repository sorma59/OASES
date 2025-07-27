package com.unimib.oases.domain.model

data class VitalSign(
    val name: String,
    val acronym: String = "",
    val unit: String = "",
    val precision: String = NumericPrecision.INTEGER.displayName,
)

enum class NumericPrecision(val displayName: String){
    INTEGER("Whole number"),
    FLOAT("Decimal number");

    companion object {
        fun fromNumericPrecisionDisplayName(displayName: String): NumericPrecision {
            return NumericPrecision.entries.find { it.displayName == displayName } ?: INTEGER
        }
    }
}
