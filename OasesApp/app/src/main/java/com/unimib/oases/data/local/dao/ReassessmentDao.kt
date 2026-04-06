package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.ReassessmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReassessmentDao {
    @Upsert
    suspend fun insert(reassessment: ReassessmentEntity)

    @Delete
    suspend fun delete(reassessment: ReassessmentEntity)

    @Query("SELECT * FROM " + TableNames.REASSESSMENT + " WHERE visit_id = :visitId")
    fun getVisitReassessments(visitId: String): Flow<List<ReassessmentEntity>>

    @Query("SELECT * FROM " + TableNames.REASSESSMENT + " WHERE complaint_id = :complaintId AND visit_id = :visitId")
    fun getReassessment(visitId: String, complaintId: String): Flow<ReassessmentEntity?>
}