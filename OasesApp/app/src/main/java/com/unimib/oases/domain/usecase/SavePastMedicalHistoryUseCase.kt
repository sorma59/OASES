package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.ui.screen.medical_visit.pmh.PastHistoryState
import com.unimib.oases.ui.screen.medical_visit.pmh.toPatientDiseases
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SavePastMedicalHistoryUseCase @Inject constructor(
    private val patientDiseaseRepository: PatientDiseaseRepository
) {

    suspend operator fun invoke(state: PastHistoryState): Outcome {
        return try {
            val patientDiseases = state.toPatientDiseases()
            return patientDiseaseRepository.addPatientDiseases(patientDiseases)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "Unexpected error")
        }
    }
}