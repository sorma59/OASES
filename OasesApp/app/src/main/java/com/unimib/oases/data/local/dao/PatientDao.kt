package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Upsert
    suspend fun insert(patient: PatientEntity): Long

    @Delete
    suspend fun delete(patient: PatientEntity)

    @Query("SELECT * FROM " + TableNames.PATIENT)
    fun getPatients(): Flow<List<PatientEntity>>

//    @Query("UPDATE " + TableNames.PATIENT + " SET status = :triageState WHERE id = :patientId")
//    fun updateTriageState(patientId: String, triageState: String)

    @Query("UPDATE " + TableNames.PATIENT + " SET status = :status WHERE id = :patientId")
    suspend fun updateStatus(patientId: String, status: String)

    @Query("SELECT * FROM " + TableNames.PATIENT + " WHERE id = :id")
    fun getPatientById(id: String): Flow<PatientEntity?>
}