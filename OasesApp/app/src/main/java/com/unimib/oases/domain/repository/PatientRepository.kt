package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PatientRepository {
    suspend fun addPatient(patient: Patient): Outcome<String>

    suspend fun addPatientAndCreateVisit(patient: Patient, visit: Visit): Outcome<PatientAndVisitIds>
    suspend fun deletePatient(patient: Patient): Outcome<Unit>
    fun deletePatientById(patientId: String): Outcome<Unit>
    fun getPatientById(patientId: String): Flow<Resource<Patient>>
//    suspend fun updateTriageState(patient: Patient, triageState: String): Resource<Unit>
    fun getPatients(): Flow<Resource<List<Patient>>>
    fun getPatientsAndVisitsOn(date: LocalDate): Flow<Resource<List<PatientWithVisitInfo>>>
    fun doOnlineTasks()
    fun doOfflineTasks()
}