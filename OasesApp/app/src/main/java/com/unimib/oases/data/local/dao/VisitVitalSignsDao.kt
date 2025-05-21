package com.unimib.oases.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.model.VitalSignsEntity
import kotlinx.coroutines.flow.Flow

interface VisitVitalSignsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visitVitalSings: VitalSignsEntity)

    @Query("DELETE FROM visit_vitalSigns WHERE visit_id = :visitId AND vitalSigns_name = :vitalSingsName AND timestamp = :timestamp")
    suspend fun delete(visitId: String, vitalSingsName: String, timestamp: String)

    @Query("SELECT vitalSigns_name  AS name FROM visit_vitalSigns WHERE visit_id = :visitId")
    fun getPatientDiseases(visitId: String): Flow<List<VitalSignsEntity>>

}