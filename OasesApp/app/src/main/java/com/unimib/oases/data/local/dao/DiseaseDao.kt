package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.DiseaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseDao {
    @Upsert
    suspend fun insert(disease: DiseaseEntity)

    @Delete
    suspend fun delete(disease: DiseaseEntity)

    @Query("SELECT * FROM " + TableNames.DISEASE + " WHERE name = :disease")
    fun getDisease(disease: String): Flow<DiseaseEntity?>

    @Query("SELECT * FROM " + TableNames.DISEASE)
    fun getAllDiseases(): Flow<List<DiseaseEntity>>
}