package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface PatientDiseaseRepository {
    suspend fun addPatientDisease(patientDisease: PatientDisease): Resource<Unit>
    suspend fun deletePatientDisease(diseaseName: String, patientId: String): Resource<Unit>
    fun getPatientDiseases(patientId: String): Flow<Resource<List<PatientDisease>>>
}