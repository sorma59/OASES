package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.VisitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visit: VisitEntity)

    @Upsert
    suspend fun upsert(visit: VisitEntity)

    @Query("SELECT * FROM " + TableNames.VISIT + " WHERE patient_id = :patientId")
    fun getVisits(patientId: String): Flow<List<VisitEntity>>

    @Query("SELECT * FROM " + TableNames.VISIT + " WHERE patient_id = :patientId AND status <> :excludedStatus ORDER BY date DESC LIMIT 1")
    fun getCurrentVisit(patientId: String, excludedStatus: String): VisitEntity?

}