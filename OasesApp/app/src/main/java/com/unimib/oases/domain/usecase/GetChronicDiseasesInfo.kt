package com.unimib.oases.domain.usecase

import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.ui.screen.nurse_assessment.history.PatientDiseaseState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetChronicDiseasesInfo  @Inject constructor(
    private val getAllChronicDiseasesUseCase: GetAllChronicDiseasesUseCase,
    private val getPatientChronicDiseasesMapUseCase: GetPatientChronicDiseasesMapUseCase,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
){
    suspend operator fun invoke(patientId: String): List<PatientDiseaseState> {
        return withContext(ioDispatcher){
            val diseasesDeferred = async { getAllChronicDiseasesUseCase() }
            val patientDiseasesDeferred = async { getPatientChronicDiseasesMapUseCase(patientId) }
            val diseases = diseasesDeferred.await()
            val patientDiseases = patientDiseasesDeferred.await()
            return@withContext diseases.map { disease ->
                patientDiseases[disease.name]?.toState() ?: disease.toState()
            }
        }
    }

    private fun PatientDisease.toState(): PatientDiseaseState {
        return PatientDiseaseState(
            disease = diseaseName,
            isDiagnosed = isDiagnosed,
            additionalInfo = additionalInfo,
            date = diagnosisDate
        )
    }

    private fun Disease.toState() : PatientDiseaseState {
        return PatientDiseaseState(
            disease = name
        )
    }
}