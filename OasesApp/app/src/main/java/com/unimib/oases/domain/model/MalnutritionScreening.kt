package com.unimib.oases.domain.model

import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningState
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MuacState
import java.util.UUID

data class MalnutritionScreening(

    val visitId: String = UUID.randomUUID().toString(),

    val weight: Double, // Kg
    val height: Double, // cm
    val muac: Muac,
) {
    val bmi: Double
        get() = weight / ((height / 100) * (height / 100))
}

fun MalnutritionScreening?.toState(): MalnutritionScreeningState {
    return this?.let {
        MalnutritionScreeningState(
            weight = it.weight.toString(),
            height = it.height.toString(),
            muacState = MuacState(it.muac.toString()),
            bmi = it.bmi
        )
    } ?: MalnutritionScreeningState()
}