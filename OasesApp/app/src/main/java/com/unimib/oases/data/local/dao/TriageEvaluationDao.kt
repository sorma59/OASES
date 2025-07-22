package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.TriageEvaluationEntity

@Dao
interface TriageEvaluationDao {
    @Upsert
    suspend fun insert(triageEvaluation: TriageEvaluationEntity)

    @Query("SELECT * FROM " + TableNames.TRIAGE_EVALUATION + " WHERE visit_id = :visitId")
    fun getTriageEvaluation(visitId: String): TriageEvaluationEntity
}