package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatientUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
    private val visitRepository: VisitRepository
) {

    fun getPatients(): Flow<Resource<List<Patient>>> {
        return patientRepository.getPatients()
    }

    fun getPatient(patientId: String): Flow<Resource<Patient>> {
        return patientRepository.getPatientById(patientId)
    }

    suspend fun deletePatient(patient: Patient): Outcome {
       return patientRepository.deletePatient(patient)
    }

//    suspend fun updateTriageState(patient: Patient, triageState: String): Resource<Unit> {
//        return patientRepository.updateTriageState(patient, triageState)
//    }

    suspend fun updateStatus(patientId: String, status: String, code: String, room: String): Outcome {
        return patientRepository.updateStatus(patientId, status, code, room)
    }

    fun getPatientVisits(patientId: String): Flow<Resource<List<Visit>>> {
        return visitRepository.getVisits(patientId)
    }

}