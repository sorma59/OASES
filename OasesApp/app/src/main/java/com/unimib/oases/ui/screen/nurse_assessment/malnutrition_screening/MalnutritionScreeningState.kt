package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.domain.model.Muac
import com.unimib.oases.domain.model.MuacCategory

data class MalnutritionScreeningState(
    val weight: String = "", // Kg
    val height: String = "", // cm
    val bmi: Double? = null,
    val muacState: MuacState = MuacState()
)

data class MuacState(
    val value: String = "",
    val category: MuacCategory? = null,
)

fun MalnutritionScreeningState.toBmiOrNull(): Double? {
    val weight = weight.toDoubleOrNull() ?: return null
    val height = height.toDoubleOrNull() ?: return null
    return weight / ((height / 100) * (height / 100))
}

fun MalnutritionScreeningState.toMuacCategoryOrNull(): MuacCategory? {
    val muac = muacState.value.toDoubleOrNull() ?: return null
    return Muac(muac).color
}

fun MalnutritionScreeningState.toMalnutritionScreeningOrNull(): MalnutritionScreening? {
    val weight = this.weight.toDoubleOrNull()
    val height = this.height.toDoubleOrNull()
    val muacValue = this.muacState.value.toDoubleOrNull()
    if (weight == null || height == null || muacValue == null)
        return null
    return MalnutritionScreening(
        weight = weight,
        height = height,
        muac = Muac(muacValue)
    )
}