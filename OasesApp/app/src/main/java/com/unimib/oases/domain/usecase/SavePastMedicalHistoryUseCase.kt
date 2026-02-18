package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.ui.screen.nurse_assessment.history.PatientDiseaseState
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SavePastMedicalHistoryUseCase @Inject constructor(
    private val patientDiseaseRepository: PatientDiseaseRepository
) {

    suspend operator fun invoke(diseasesStates: List<PatientDiseaseState>, patientId: String): Outcome<Unit> {
        return try {
            val patientDiseases = diseasesStates.toPatientDiseases(patientId)
            return patientDiseaseRepository.addPatientDiseases(patientDiseases)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "Unexpected error")
        }
    }

    private fun List<PatientDiseaseState>.toPatientDiseases(patientId: String): List<PatientDisease> {
        return this
            .filter { it.isDiagnosed != null || it.freeTextValue.isNotBlank() }
            .map {
                PatientDisease(
                    patientId = patientId,
                    diseaseName = it.disease,
                    isDiagnosed = it.isDiagnosed,
                    diagnosisDate = it.date,
                    additionalInfo = it.additionalInfo,
                    freeTextValue = it.freeTextValue
                )
            }
    }
}