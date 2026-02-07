package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.VisitVitalSignEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitVitalSignDao {

    @Upsert
    suspend fun insert(visitVitalSings: VisitVitalSignEntity)

    @Upsert
    suspend fun insertAll(visitVitalSigns: List<VisitVitalSignEntity>)
    @Delete
    suspend fun delete(visitVitalSign: VisitVitalSignEntity)

    @Query("DELETE FROM " + TableNames.VISIT_VITAL_SIGN + " WHERE visit_id = :visitId AND vital_sign_name = :vitalSignName AND timestamp = :timestamp")
    suspend fun deleteById(visitId: String, vitalSignName: String, timestamp: String)

    @Query("SELECT * FROM " + TableNames.VISIT_VITAL_SIGN + " WHERE visit_id = :visitId ORDER BY timestamp")
    fun getVisitVitalSigns(visitId: String): Flow<List<VisitVitalSignEntity>>

    @Query("SELECT vs.* FROM " + TableNames.VISIT_VITAL_SIGN + " vs JOIN (SELECT vital_sign_name, MAX(timestamp) AS latest_time FROM ${TableNames.VISIT_VITAL_SIGN} WHERE visit_id = :visitId GROUP BY vital_sign_name) latest ON vs.vital_sign_name = latest.vital_sign_name AND vs.timestamp = latest.latest_time WHERE vs.visit_id = :visitId")
    fun getVisitLatestVitalSigns(visitId: String): Flow<List<VisitVitalSignEntity>>
}