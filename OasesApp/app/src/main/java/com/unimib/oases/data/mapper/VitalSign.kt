package com.unimib.oases.data.mapper

import com.unimib.oases.data.model.DiseaseEntity
import com.unimib.oases.data.model.VitalSignsEntity
import com.unimib.oases.domain.model.VitalSign

fun VitalSign.toEntity(): VitalSignsEntity {
    return VitalSignsEntity(
        name = name
    )
}

fun VitalSignsEntity.toVitalSign(): VitalSign {
    return VitalSign(
        name = name
    )
}