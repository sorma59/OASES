package com.unimib.oases.domain.usecase

import android.util.Log
import com.unimib.oases.data.bluetooth.transfer.PatientFullData
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HandleReceivedPatientWithTriageDataUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
    private val visitRepository: VisitRepository,
    private val patientDiseaseRepository: PatientDiseaseRepository,
    private val visitVitalSignRepository: VisitVitalSignRepository,
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val malnutritionScreeningRepository: MalnutritionScreeningRepository
) {
    suspend operator fun invoke(patientWithTriageData: PatientFullData) = withContext(Dispatchers.IO) {
        try {
            // Add the patient to the db
            if (patientRepository.addPatient(patientWithTriageData.patientDetails) is Resource.Error)
                throw Exception("Failed to insert patient ${patientWithTriageData.patientDetails}")

            // Add the visit to the db
            if (visitRepository.addVisit(patientWithTriageData.visit) is Resource.Error)
                throw Exception("Failed to insert visit ${patientWithTriageData.visit}")

            // Add the patient diseases to the db
            patientWithTriageData.patientDiseases.forEach {
                if (patientDiseaseRepository.addPatientDisease(it) is Resource.Error)
                    throw Exception("Failed to insert patient disease $it")
            }

            // Add the visit vital signs to the db
            patientWithTriageData.vitalSigns.forEach {
                if (visitVitalSignRepository.addVisitVitalSign(it) is Resource.Error)
                    throw Exception("Failed to insert visit vital sign $it")
            }

            // Add the triage evaluation to the db
            if (triageEvaluationRepository.insertTriageEvaluation(patientWithTriageData.triageEvaluation) is Resource.Error)
                throw Exception("Failed to insert triage evaluation ${patientWithTriageData.triageEvaluation}")

            if (patientWithTriageData.malnutritionScreening != null)
                if (malnutritionScreeningRepository.insertMalnutritionScreening(patientWithTriageData.malnutritionScreening) is Resource.Error)
                    throw Exception("Failed to insert malnutrition screening ${patientWithTriageData.malnutritionScreening}")

        } catch (e: Exception) {
            Log.e("PatientWithTriageDataInsertUseCase", "Failed to insert patient full data", e)
        }
    }
}