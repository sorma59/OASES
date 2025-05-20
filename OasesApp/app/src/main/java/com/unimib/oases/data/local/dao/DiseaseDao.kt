package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.model.DiseaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(disease: DiseaseEntity)

    @Query("SELECT * FROM diseases")
    fun getAllDiseases(): Flow<List<DiseaseEntity>>
}