package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.DiseaseEntity
import com.unimib.oases.domain.model.Disease

fun Disease.toEntity(): DiseaseEntity {
    return DiseaseEntity(
        name = name
    )
}

fun DiseaseEntity.toDisease(): Disease {
    return Disease(
        name = name
    )
}