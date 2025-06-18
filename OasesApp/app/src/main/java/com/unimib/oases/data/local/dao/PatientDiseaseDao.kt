package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.PatientDiseaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDiseaseDao {
    @Upsert
    suspend fun insert(patientDisease: PatientDiseaseEntity)

    @Query("DELETE FROM " + TableNames.PATIENT_DISEASE + " WHERE patient_id = :patientId AND disease_name = :diseaseName")
    suspend fun delete(patientId: String, diseaseName: String)

    @Query("SELECT * FROM " + TableNames.PATIENT_DISEASE + " WHERE patient_id = :patientId")
    fun getPatientDiseases(patientId: String): Flow<List<PatientDiseaseEntity>>
}