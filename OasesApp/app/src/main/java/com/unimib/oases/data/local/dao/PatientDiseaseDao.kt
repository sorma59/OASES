package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unimib.oases.data.model.DiseaseEntity
import com.unimib.oases.data.model.PatientDiseaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDiseaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patientDisease: PatientDiseaseEntity)

    @Query("DELETE FROM patient_diseases WHERE patient_id = :patientId AND disease_name = :diseaseName")
    suspend fun delete(patientId: String, diseaseName: String)

    // What to do?
    @Query("SELECT disease_name AS name FROM patient_diseases WHERE patient_id = :patientId")
    fun getPatientDiseases(patientId: String): Flow<List<DiseaseEntity>>
}