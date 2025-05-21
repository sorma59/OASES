package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.model.DiseaseEntity
import com.unimib.oases.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(disease: DiseaseEntity)

    @Delete
    fun delete(disease: DiseaseEntity)

    @Query("SELECT * FROM diseases WHERE name = :disease")
    fun getDisease(disease: String): Flow<DiseaseEntity?>

    @Query("SELECT * FROM diseases")
    fun getAllDiseases(): Flow<List<DiseaseEntity>>
}