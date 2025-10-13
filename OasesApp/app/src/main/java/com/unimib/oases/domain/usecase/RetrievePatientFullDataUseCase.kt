package com.unimib.oases.domain.usecase

import android.util.Log
import com.unimib.oases.domain.model.PatientFullData
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.domain.repository.TriageEvaluationRepository
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstNullableSuccess
import com.unimib.oases.util.firstSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
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

            withContext(Dispatchers.IO){
                coroutineScope {
//                    val patientTime = System.currentTimeMillis()

                    // Patient details
                    val patientDeferred = async {
                        patientRepository.getPatientById(patientId).firstSuccess()
                    }

//                    Log.d("Prova: patient", (System.currentTimeMillis() - patientTime).toString())
//
//                    val visitTime = System.currentTimeMillis()

                    // Current visit
                    val visitDeferred = async {
                        getCurrentVisitUseCase(patientId).firstNullableSuccess()
                    }

//                    Log.d("Prova: visit", (System.currentTimeMillis() - visitTime).toString())
//
//                    val diseases = System.currentTimeMillis()

                    // Patient's PMH
                    val patientDiseasesDeferred = async {
                        val patientDiseasesResource =
                            patientDiseaseRepository.getPatientDiseases(patientId).first {
                                it is Resource.Success || it is Resource.Error
                            }
                        if (patientDiseasesResource is Resource.Error)
                            throw Exception(patientDiseasesResource.message)
                        (patientDiseasesResource as Resource.Success).data!!
                    }

//                    Log.d("Prova: diseases", (System.currentTimeMillis() - diseases).toString())
//
//                    val preVitals = System.currentTimeMillis()

                    val visit = visitDeferred.await()

                    if (visit == null)
                        return@coroutineScope Resource.Success(
                            PatientFullData(
                                patientDetails = patientDeferred.await(),
                            )
                        )

                    // Current visit's vital signs
                    val vitalSignsDeferred = async {
                        val vitalSignsResource =
                            visitVitalSignRepository.getVisitVitalSigns(visit.id).first {
                                it is Resource.Success || it is Resource.Error
                            }
                        if (vitalSignsResource is Resource.Error)
                            throw Exception(vitalSignsResource.message)
                        (vitalSignsResource as Resource.Success).data!!
                    }

//                    Log.d("Prova: vitals", (System.currentTimeMillis() - preVitals).toString())
//
//                    val triage = System.currentTimeMillis()

                    // Current visit's triage evaluation
                    val triageEvaluationDeferred = async {
                        val triageEvaluationResource =
                            triageEvaluationRepository.getTriageEvaluation(visit.id).first {
                                it is Resource.Success || it is Resource.Error
                            }
                        if (triageEvaluationResource is Resource.Error)
                            throw Exception(triageEvaluationResource.message)
                        (triageEvaluationResource as Resource.Success).data!!
                    }

//                    Log.d("Prova: triage", (System.currentTimeMillis() - triage).toString())
//
//                    val malnutrition = System.currentTimeMillis()

                    // Current visit's malnutrition screening
                    val malnutritionScreeningDeferred = async {
                        val malnutritionScreeningResource =
                            malnutritionScreeningRepository.getMalnutritionScreening(visit.id)
                                .first {
                                    it is Resource.Success || it is Resource.Error
                                }
                        if (malnutritionScreeningResource is Resource.Error)
                            throw Exception(malnutritionScreeningResource.message)
                        (malnutritionScreeningResource as Resource.Success).data
                    }

//                    Log.d(
//                        "Prova: malnutrition",
//                        (System.currentTimeMillis() - malnutrition).toString()
//                    )
//
//                    val summaries = System.currentTimeMillis()

                    // Current visit's complaints summaries
                    val complaintsSummariesDeferred = async {
                        val complaintsSummariesResource =
                            complaintSummaryRepository.getVisitComplaintsSummaries(visit.id).first {
                                it is Resource.Success || it is Resource.Error
                            }
                        if (complaintsSummariesResource is Resource.Error)
                            throw Exception(complaintsSummariesResource.message)
                        (complaintsSummariesResource as Resource.Success).data!!
                    }

//                    Log.d("Prova: summaries", (System.currentTimeMillis() - summaries).toString())

                    val patient = patientDeferred.await()
                    val patientDiseases = patientDiseasesDeferred.await()
                    val vitalSigns = vitalSignsDeferred.await()
                    val triageEvaluation = triageEvaluationDeferred.await()
                    val malnutritionScreening = malnutritionScreeningDeferred.await()
                    val complaintsSummaries = complaintsSummariesDeferred.await()

//                    Log.d("Prova: total", (System.currentTimeMillis() - patientTime).toString())

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
                }
            }
        } catch (e: Exception) {
            Log.e("RetrieveFullDataUseCase", "Error during the retrieval of patient full data: ${e.message}")
            Resource.Error(e.message ?: "An error occurred during the retrieval of patient full data")
        }
    }
}