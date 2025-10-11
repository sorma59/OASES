package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.DiseaseEntity
import com.unimib.oases.domain.model.AgeSpecificity.Companion.fromAgeSpecificityDisplayName
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.SexSpecificity.Companion.fromSexSpecificityDisplayName

fun Disease.toEntity(): DiseaseEntity {
    return DiseaseEntity(
        name = name,
        sexSpecificity = sexSpecificity.displayName,
        ageSpecificity = ageSpecificity.displayName
    )
}

fun DiseaseEntity.toDomain(): Disease {
    return Disease(
        name = name,
        sexSpecificity = fromSexSpecificityDisplayName(sexSpecificity),
        ageSpecificity = fromAgeSpecificityDisplayName(ageSpecificity)
    )
}