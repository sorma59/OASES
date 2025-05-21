package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.model.VisitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitEntity)

    @Upsert
    suspend fun upsertVisit(visit: VisitEntity)

    @Query("SELECT * FROM visits WHERE patient_id = :patientId")
    fun getVisits(patientId: String): Flow<List<VisitEntity>>

}