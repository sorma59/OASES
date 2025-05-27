package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.model.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: PatientEntity): Long

    @Delete
    suspend fun delete(patient: PatientEntity)

    @Query("SELECT * FROM " + TableNames.PATIENT)
    fun getPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM " + TableNames.PATIENT + " WHERE id = :id")
    fun getPatientById(id: String): Flow<PatientEntity?>
}