package com.unimib.oases.domain.usecase

import android.util.Log
import com.unimib.oases.data.bluetooth.transfer.PatientFullData
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.domain.repository.PatientRepository
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
) {
    suspend fun invoke(patientWithTriageData: PatientFullData) = withContext(Dispatchers.IO) {
        try {
            // Add the patient to the db
            val patientResult = patientRepository.addPatient(patientWithTriageData.patientDetails)
            if (patientResult is Resource.Error) {
                throw Exception("Failed to insert patient ${patientWithTriageData.patientDetails}")
            }

            // Add the visit to the db
            val visitResult = visitRepository.addVisit(patientWithTriageData.visit)
            if (visitResult is Resource.Error) {
                throw Exception("Failed to insert visit ${patientWithTriageData.visit}")
            }

            // Add the patient diseases to the db
            patientWithTriageData.patientDiseases.forEach {
                val patientDiseaseResult = patientDiseaseRepository.addPatientDisease(it)
                if (patientDiseaseResult is Resource.Error) {
                    throw Exception("Failed to insert patient disease $it")
                }
            }

            // Add the visit vital signs to the db
            patientWithTriageData.vitalSigns.forEach {
                val visitVitalSignsResult = visitVitalSignRepository.addVisitVitalSign(it)
                if (visitVitalSignsResult is Resource.Error) {
                    throw Exception("Failed to insert visit vital sign $it")
                }
            }
        } catch (e: Exception) {
            Log.e("PatientWithTriageDataInsertUseCase", "Failed to insert patient full data", e)
        }
    }
}