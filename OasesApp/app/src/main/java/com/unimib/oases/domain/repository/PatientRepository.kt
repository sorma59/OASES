package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PatientRepository {
    val newPatientEvents: SharedFlow<Patient>
    val receivedPatients: StateFlow<List<Patient>>
    suspend fun addPatient(patient: Patient): Resource<Unit>
    suspend fun removePatientFromRecentlyReceived(patient: Patient)
    suspend fun deletePatient(patient: Patient): Resource<Unit>
    suspend fun getPatientById(patientId: String): Flow<Resource<Patient?>>
    fun getPatients(): Flow<Resource<List<Patient>>>

}