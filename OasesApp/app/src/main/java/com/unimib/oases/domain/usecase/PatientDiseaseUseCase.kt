package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class PatientDiseaseUseCase @Inject constructor(
    private val patientDiseaseRepository: PatientDiseaseRepository
) {

    suspend fun addPatientDisease(patientDisease: PatientDisease): Resource<Unit> {
        return patientDiseaseRepository.addPatientDisease(patientDisease)
    }

    suspend fun deletePatientDisease(diseaseName: String, patientId: String): Resource<Unit> {
        return patientDiseaseRepository.deletePatientDisease(diseaseName, patientId)
    }

    fun getPatientDiseases(patientId: String): Flow<Resource<List<PatientDisease>>> {
        return patientDiseaseRepository.getPatientDiseases(patientId)
    }
}