package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.MalnutritionScreeningEntity
import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.domain.model.Muac

fun MalnutritionScreening.toEntity(): MalnutritionScreeningEntity {
    return MalnutritionScreeningEntity(
        visitId = visitId,
        weightInKg = weight,
        heightInCm = height,
        muacInCm = muac.value,
        muacCategory = muac.color.name,
        bmi = bmi,
    )
}

fun MalnutritionScreeningEntity.toDomain(): MalnutritionScreening {
    return MalnutritionScreening(
        visitId = visitId,
        weight = weightInKg,
        height = heightInCm,
        muac = Muac(muacInCm)
    )
}