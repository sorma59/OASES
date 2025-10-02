package com.unimib.oases.domain.model

data class Muac(
    val value: Double
) {
    val color: MuacCategory
        get() = when {
            value < 21 -> MuacCategory.SEVERE
            value < 25.5 -> MuacCategory.MODERATE
            else -> MuacCategory.NORMAL
        }
}

enum class MuacCategory(val color: String) {
    NORMAL("Normal"),
    MODERATE("Moderate"),
    SEVERE("Severe")
}