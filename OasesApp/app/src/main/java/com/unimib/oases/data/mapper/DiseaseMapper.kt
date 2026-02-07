package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.DiseaseEntity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.DiseaseEntryType

fun Disease.toEntity(): DiseaseEntity {
    return DiseaseEntity(
        name = name,
        sexSpecificity = sexSpecificity,
        ageSpecificity = ageSpecificity,
        group = group,
        allowsFreeText = entryType == DiseaseEntryType.FREE_TEXT
    )
}

fun DiseaseEntity.toDomain(): Disease {
    return Disease(
        name = name,
        sexSpecificity = sexSpecificity,
        ageSpecificity = ageSpecificity,
        group = group,
        entryType = if (allowsFreeText) DiseaseEntryType.FREE_TEXT else DiseaseEntryType.SELECTION
    )
}