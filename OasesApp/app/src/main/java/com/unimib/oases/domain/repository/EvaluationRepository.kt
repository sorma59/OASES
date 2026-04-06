package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface EvaluationRepository {
    suspend fun addEvaluation(evaluation: Evaluation): Outcome<Unit>
    suspend fun addEvaluations(evaluations: List<Evaluation>): Outcome<Unit>
    suspend fun deleteEvaluation(evaluation: Evaluation): Outcome<Unit>
    fun getVisitEvaluations(visitId: String): Flow<Resource<List<Evaluation>>>
    fun getEvaluation(visitId: String, complaintId: String): Flow<Resource<Evaluation>>
}