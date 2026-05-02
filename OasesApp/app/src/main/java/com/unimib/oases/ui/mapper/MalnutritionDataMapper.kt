package com.unimib.oases.ui.mapper

import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningData
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MuacState

fun MalnutritionScreening?.toState(): MalnutritionScreeningData? {
    return this?.let {
        MalnutritionScreeningData(
            weight = it.weight.toString(),
            height = it.height.toString(),
            muacState = MuacState(it.muac.value.toString(), it.muac.color),
            bmi = it.bmi
        )
    }
}