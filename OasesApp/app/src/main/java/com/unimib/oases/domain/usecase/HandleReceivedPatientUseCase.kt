package com.unimib.oases.domain.usecase

import android.util.Log
import com.unimib.oases.domain.model.BluetoothPatientHandlingResult
import com.unimib.oases.domain.model.PatientFullData
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
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

class HandleReceivedPatientUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
    private val visitRepository: VisitRepository,
    private val patientDiseaseRepository: PatientDiseaseRepository,
    private val visitVitalSignRepository: VisitVitalSignRepository,
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val malnutritionScreeningRepository: MalnutritionScreeningRepository,
    private val complaintSummaryRepository: ComplaintSummaryRepository
) {

    val errorString = { name: String ->
        "Patient $name's data was not correctly saved on this device, try again or have it sent to this device again"
    }

    suspend operator fun invoke(patientFullData: PatientFullData): BluetoothPatientHandlingResult = withContext(Dispatchers.IO) {
        return@withContext try {
            // Add the patient to the db
            if (patientRepository.addPatient(patientFullData.patientDetails) is Resource.Error)
                throw Exception("Failed to insert patient ${patientFullData.patientDetails}")

            patientFullData.visit?.let {
                // Add the visit to the db
                if (visitRepository.addVisit(it) is Resource.Error)
                    throw Exception("Failed to insert visit $it")
            }

            // Add the patient diseases to the db
            patientFullData.patientDiseases.forEach {
                if (patientDiseaseRepository.addPatientDisease(it) is Resource.Error)
                    throw Exception("Failed to insert patient disease $it")
            }

            // Add the visit vital signs to the db
            patientFullData.vitalSigns.forEach {
                if (visitVitalSignRepository.addVisitVitalSign(it) is Resource.Error)
                    throw Exception("Failed to insert visit vital sign $it")
            }

            patientFullData.triageEvaluation?.let {
                // Add the triage evaluation to the db
                if (triageEvaluationRepository.insertTriageEvaluation(it) is Resource.Error)
                    throw Exception("Failed to insert triage evaluation $it")
            }

            patientFullData.malnutritionScreening?.let {
                // Add the malnutrition screening to the db
                if (malnutritionScreeningRepository.insertMalnutritionScreening(patientFullData.malnutritionScreening) is Resource.Error)
                    throw Exception("Failed to insert malnutrition screening ${patientFullData.malnutritionScreening}")
            }

            // Add the complaint summaries to the db
            patientFullData.complaintsSummaries.forEach {
                if (complaintSummaryRepository.addComplaintSummary(it) is Resource.Error)
                    throw Exception("Failed to insert complaint summary $it")
            }
            BluetoothPatientHandlingResult.PatientReceived(patientFullData.patientDetails)
        } catch (e: Exception) {
            Log.e("PatientWithTriageDataInsertUseCase", "Failed to insert patient full data", e)
            BluetoothPatientHandlingResult.Error(
                errorString(patientFullData.patientDetails.name)
            )
        }
    }
}