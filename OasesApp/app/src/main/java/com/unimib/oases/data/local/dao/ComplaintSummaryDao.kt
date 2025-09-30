package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.ComplaintSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintSummaryDao {

    @Upsert
    suspend fun insert(complaintSummary: ComplaintSummaryEntity)

    @Delete
    suspend fun delete(complaintSummary: ComplaintSummaryEntity)

    @Query("SELECT * FROM ${TableNames.COMPLAINT_SUMMARY} WHERE visit_id = :visitId")
    fun getVisitComplaintsSummaries(visitId: String): Flow<List<ComplaintSummaryEntity>>

    @Query("SELECT * FROM ${TableNames.COMPLAINT_SUMMARY} WHERE complaint_id = :complaintId AND visit_id = :visitId")
    fun getComplaintSummary(visitId: String, complaintId: String): Flow<ComplaintSummaryEntity?>
}