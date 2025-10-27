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

                    // Patient details
                    val patientDeferred = async {
                        patientRepository
                            .getPatientById(patientId)
                            .firstSuccess()
                    }

                    // Current visit
                    val visitDeferred = async {
                        getCurrentVisitUseCase(patientId).firstNullableSuccess()
                    }

                    // Patient's PMH
                    val patientDiseasesDeferred = async {
                        patientDiseaseRepository
                            .getPatientDiseases(patientId)
                            .firstSuccess()
                    }

                    val visit = visitDeferred.await()

                    if (visit == null)
                        return@coroutineScope Resource.Success(
                            PatientFullData(
                                patientDetails = patientDeferred.await(),
                            )
                        )

                    // Current visit's vital signs
                    val vitalSignsDeferred = async {
                        visitVitalSignRepository
                            .getVisitVitalSigns(visit.id)
                            .firstSuccess()
                    }

                    // Current visit's triage evaluation
                    val triageEvaluationDeferred = async {
                        triageEvaluationRepository
                            .getTriageEvaluation(visit.id)
                            .firstSuccess()
                    }

                    // Current visit's malnutrition screening
                    val malnutritionScreeningDeferred = async {
                        malnutritionScreeningRepository
                            .getMalnutritionScreening(visit.id)
                            .firstNullableSuccess()
                    }

                    // Current visit's complaints summaries
                    val complaintsSummariesDeferred = async {
                        complaintSummaryRepository
                            .getVisitComplaintsSummaries(visit.id)
                            .firstSuccess()
                    }

                    val patient = patientDeferred.await()
                    val patientDiseases = patientDiseasesDeferred.await()
                    val vitalSigns = vitalSignsDeferred.await()
                    val triageEvaluation = triageEvaluationDeferred.await()
                    val malnutritionScreening = malnutritionScreeningDeferred.await()
                    val complaintsSummaries = complaintsSummariesDeferred.await()

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
        } catch (e: NoSuchElementException){
            Resource.NotFound(e.message ?: "Patient not found")
        }
        catch (e: Exception) {
            Log.e("RetrieveFullDataUseCase", "Error during the retrieval of patient full data: ${e.message}")
            Resource.Error(e.message ?: "An error occurred during the retrieval of patient full data")
        }
    }
}