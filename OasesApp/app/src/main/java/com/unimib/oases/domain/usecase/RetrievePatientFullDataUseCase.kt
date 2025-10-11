package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientFullData
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RetrievePatientFullDataUseCase @Inject constructor(
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val patientRepository: PatientRepository,
    private val patientDiseaseRepository: PatientDiseaseRepository,
    private val visitVitalSignRepository: VisitVitalSignRepository,
    private val triageEvaluationRepository: TriageEvaluationRepository,
    private val malnutritionScreeningRepository: MalnutritionScreeningRepository,
    private val complaintSummaryRepository: ComplaintSummaryRepository
) {

    suspend operator fun invoke(patientId: String): Resource<PatientFullData> {

        return try {
            // Patient details
            val patientResource = patientRepository.getPatientById(patientId).first {
                it is Resource.Success || it is Resource.Error
            }
            if (patientResource is Resource.Error)
                throw Exception(patientResource.message)
            val patient = (patientResource as Resource.Success).data!!

            // Current visit
            val visitResource = getCurrentVisitUseCase(patientId)
            if (visitResource is Resource.Error)
                throw Exception(visitResource.message)
            val visit = (visitResource as Resource.Success).data

            // Patient's PMH
            val patientDiseasesResource =
                patientDiseaseRepository.getPatientDiseases(patientId).first {
                    it is Resource.Success || it is Resource.Error
                }
            if (patientDiseasesResource is Resource.Error)
                throw Exception(patientDiseasesResource.message)
            val patientDiseases = (patientDiseasesResource as Resource.Success).data!!

            // Current visit's vital signs
            val vitalSigns = visit?.let {
                val vitalSignsResource = visitVitalSignRepository.getVisitVitalSigns(it.id).first {
                    it is Resource.Success || it is Resource.Error
                }
                if (vitalSignsResource is Resource.Error)
                    throw Exception(vitalSignsResource.message)
                (vitalSignsResource as Resource.Success).data
            } ?: emptyList()

            // Current visit's triage evaluation
            val triageEvaluation = visit?.let {
                val triageEvaluationResource = triageEvaluationRepository.getTriageEvaluation(it.id)
                if (triageEvaluationResource is Resource.Error)
                    throw Exception(triageEvaluationResource.message)
                (triageEvaluationResource as Resource.Success).data
            }

            // Current visit's malnutrition screening
            val malnutritionScreening = visit?.let {
                val malnutritionScreeningResource =
                    malnutritionScreeningRepository.getMalnutritionScreening(it.id)
                if (malnutritionScreeningResource is Resource.Error)
                    throw Exception(malnutritionScreeningResource.message)
                (malnutritionScreeningResource as Resource.Success).data
            }

            // Current visit's complaints summaries
            val complaintsSummaries = visit?.let {
                val complaintsSummariesResource =
                    complaintSummaryRepository.getVisitComplaintsSummaries(it.id).first {
                        it is Resource.Success || it is Resource.Error
                    }
                if (complaintsSummariesResource is Resource.Error)
                    throw Exception(complaintsSummariesResource.message)
                (complaintsSummariesResource as Resource.Success).data
            } ?: emptyList()

            Resource.Success(
                PatientFullData(
                    patientDetails = patient,
                    visit = visit,
                    patientDiseases = patientDiseases,
                    vitalSigns = vitalSigns,
                    triageEvaluation = triageEvaluation,
                    malnutritionScreening = malnutritionScreening,
                    complaintsSummaries = complaintsSummaries
                )
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred during the retrieval of patient full data")
        }
    }
}