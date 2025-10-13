package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface TriageEvaluationRepository {
    suspend fun insertTriageEvaluation(triageEvaluation: TriageEvaluation): Resource<Unit>
    fun getTriageEvaluation(visitId: String): Flow<Resource<TriageEvaluation>>
}