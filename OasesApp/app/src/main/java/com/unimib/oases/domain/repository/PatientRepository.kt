package com.unimib.oases.domain.repository

import com.unimib.oases.data.model.Patient
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PatientRepository {
    val newPatientEvents: SharedFlow<Patient>
    val receivedPatients: SharedFlow<Patient>
    suspend fun addPatient(patient: Patient): Resource<Unit>
    fun getPatients(): Flow<Resource<List<Patient>>>
}