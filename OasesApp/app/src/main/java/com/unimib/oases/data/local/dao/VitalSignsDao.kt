package com.unimib.oases.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.model.VitalSignsEntity
import kotlinx.coroutines.flow.Flow

interface VitalSignsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vitalSings: VitalSignsEntity)

    @Query("SELECT * FROM vitalSings")
    fun getAllVitalSings(): Flow<List<VitalSignsEntity>>
}