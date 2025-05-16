package com.unimib.oases.domain.usecase

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



}