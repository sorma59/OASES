package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.domain.model.TriageEvaluation

fun TriageEvaluation.toEntity(): TriageEvaluationEntity {
    return TriageEvaluationEntity(
        visitId = visitId,
        redSymptomIds = redSymptomIds,
        yellowSymptomIds = yellowSymptomIds,
    )
}

fun TriageEvaluationEntity.toDomain(): TriageEvaluation {
    return TriageEvaluation(
        visitId = visitId,
        redSymptomIds = redSymptomIds,
        yellowSymptomIds = yellowSymptomIds,
    )
}