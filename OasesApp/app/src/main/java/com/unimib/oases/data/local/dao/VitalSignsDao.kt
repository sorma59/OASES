package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.model.DiseaseEntity
import com.unimib.oases.data.model.VitalSignsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface VitalSignsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vitalSings: VitalSignsEntity)

    @Delete
    fun delete(vitalSign: VitalSignsEntity)

    @Query("SELECT * FROM vitalSigns WHERE name = :vitalSign")
    fun getVitalSign(vitalSign: String): Flow<VitalSignsEntity?>


    @Query("SELECT * FROM vitalSigns")
    fun getAllVitalSigns(): Flow<List<VitalSignsEntity>>
}