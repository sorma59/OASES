package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.VisitEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface VisitDao {

    @Upsert
    suspend fun insert(visit: VisitEntity)

    @Upsert
    suspend fun upsert(visit: VisitEntity)

    @Query("SELECT * FROM " + TableNames.VISIT + " WHERE patient_id = :patientId")
    fun getVisits(patientId: String): Flow<List<VisitEntity>>

    @Query("SELECT * FROM " + TableNames.VISIT + " WHERE patient_id = :patientId AND `date` = :today LIMIT 1")
    fun getCurrentVisit(patientId: String, today: String = LocalDate.now().toString()): VisitEntity?

}