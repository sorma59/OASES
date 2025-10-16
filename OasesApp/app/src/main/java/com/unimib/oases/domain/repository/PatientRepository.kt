package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PatientRepository {
    val newPatientEvents: SharedFlow<Patient>
    suspend fun addPatient(patient: Patient): Resource<Unit>
    suspend fun deletePatient(patient: Patient): Resource<Unit>
    fun deletePatientById(patientId: String): Resource<Unit>
    fun getPatientById(patientId: String): Flow<Resource<Patient>>
//    suspend fun updateTriageState(patient: Patient, triageState: String): Resource<Unit>
    suspend fun updateStatus(patient: Patient, status: String, code: String, room: String): Resource<Unit>
    fun getPatients(): Flow<Resource<List<Patient>>>
    fun doOnlineTasks()
    fun doOfflineTasks()
}