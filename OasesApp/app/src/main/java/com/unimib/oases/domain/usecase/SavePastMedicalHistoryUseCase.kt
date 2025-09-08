package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.ui.screen.medical_visit.pmh.PastHistoryState
import com.unimib.oases.ui.screen.medical_visit.pmh.toPatientDiseases
import com.unimib.oases.util.Resource
import javax.inject.Inject

class SavePastMedicalHistoryUseCase @Inject constructor(
    private val patientDiseaseRepository: PatientDiseaseRepository
) {

    suspend operator fun invoke(state: PastHistoryState): Resource<Unit> {
        return try {
            val patientDiseases = state.toPatientDiseases()
            return patientDiseaseRepository.addPatientDiseases(patientDiseases)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unexpected error")
        }
    }
}