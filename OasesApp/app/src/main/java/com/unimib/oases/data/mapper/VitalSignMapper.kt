package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.VitalSignEntity
import com.unimib.oases.domain.model.NumericPrecision.Companion.fromNumericPrecisionDisplayName
import com.unimib.oases.domain.model.VitalSign

fun VitalSign.toEntity(): VitalSignEntity {
    return VitalSignEntity(
        name = name,
        acronym = acronym,
        unit = unit,
        precision = fromNumericPrecisionDisplayName(precision)
    )
}

fun VitalSignEntity.toDomain(): VitalSign {
    return VitalSign(
        name = name,
        acronym = acronym,
        unit = unit,
        precision = precision.displayName
    )
}