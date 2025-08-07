package com.unimib.oases.domain.model

data class MalnutritionScreening(
    val weight: Double, // Kg
    val height: Double, // cm
    val muac: Muac,
) {
    val bmi: Double
        get() = weight / ((height / 100) * (height / 100))
}