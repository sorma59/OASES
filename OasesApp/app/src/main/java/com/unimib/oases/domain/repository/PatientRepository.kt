package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PatientRepository {
    val newPatientEvents: SharedFlow<Patient>
    suspend fun addPatient(patient: Patient): Outcome
    suspend fun deletePatient(patient: Patient): Outcome
    fun deletePatientById(patientId: String): Outcome
    fun getPatientById(patientId: String): Flow<Resource<Patient>>
//    suspend fun updateTriageState(patient: Patient, triageState: String): Resource<Unit>
    suspend fun updateStatus(patient: Patient, status: String, code: String, room: String): Outcome
    fun getPatients(): Flow<Resource<List<Patient>>>
    fun doOnlineTasks()
    fun doOfflineTasks()
}