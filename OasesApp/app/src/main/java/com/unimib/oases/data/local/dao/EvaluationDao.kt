package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.EvaluationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluationDao {

    @Upsert
    suspend fun insert(evaluation: EvaluationEntity)

    @Upsert
    suspend fun insertAll(evaluations: List<EvaluationEntity>)

    @Delete
    suspend fun delete(evaluation: EvaluationEntity)

    @Query("SELECT * FROM " + TableNames.EVALUATION + " WHERE visit_id = :visitId")
    fun getVisitEvaluations(visitId: String): Flow<List<EvaluationEntity>>

    @Query("SELECT * FROM " + TableNames.EVALUATION + " WHERE complaint_id = :complaintId AND visit_id = :visitId")
    fun getEvaluation(visitId: String, complaintId: String): Flow<EvaluationEntity?>
}