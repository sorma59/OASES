package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.MalnutritionScreeningEntity

@Dao
interface MalnutritionScreeningDao {
    @Upsert
    suspend fun insert(malnutritionScreening: MalnutritionScreeningEntity)

    @Query("SELECT * FROM " + TableNames.MALNUTRITION_SCREENING + " WHERE visit_id = :visitId LIMIT 1")
    fun getMalnutritionScreening(visitId: String): MalnutritionScreeningEntity?
}