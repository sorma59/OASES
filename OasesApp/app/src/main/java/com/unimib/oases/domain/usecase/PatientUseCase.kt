package com.unimib.oases.domain.usecase

import com.unimib.oases.data.model.User
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatientUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {

    suspend fun getPatients(): Flow<Resource<List<Patient>>> {
        return patientRepository.getPatients()
    }

    suspend fun getPatient(patientId: String): Flow<Resource<Patient?>> {
        return patientRepository.getPatientById(patientId)
    }


    suspend fun deletePatient(patient: Patient): Resource<Unit> {
       return patientRepository.deletePatient(patient)
    }


}