package com.unimib.oases.data.local.dao

import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.model.TriageEvaluationEntity

interface TriageEvaluationDao {
    @Upsert
    suspend fun insert(triageEvaluation: TriageEvaluationEntity)

    @Query("SELECT * FROM triage_evaluation WHERE visit_id = :visitId")
    fun getTriageEvaluation(visitId: String): TriageEvaluationEntity
}