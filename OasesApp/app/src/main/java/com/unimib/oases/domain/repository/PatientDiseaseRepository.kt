package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface PatientDiseaseRepository {
    suspend fun addPatientDisease(patientDisease: PatientDisease): Outcome<Unit>
    suspend fun addPatientDiseases(patientDiseases: List<PatientDisease>): Outcome<Unit>
    suspend fun deletePatientDisease(diseaseName: String, patientId: String): Outcome<Unit>
    fun getPatientDiseases(patientId: String): Flow<Resource<List<PatientDisease>>>
}