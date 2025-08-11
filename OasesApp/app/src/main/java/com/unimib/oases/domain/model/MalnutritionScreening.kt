package com.unimib.oases.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class MalnutritionScreening(

    val visitId: String = UUID.randomUUID().toString(),

    val weight: Double, // Kg
    val height: Double, // cm
    val muac: Muac,
) {
    val bmi: Double
        get() = weight / ((height / 100) * (height / 100))
}