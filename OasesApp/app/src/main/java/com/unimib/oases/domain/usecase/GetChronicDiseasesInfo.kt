package com.unimib.oases.domain.usecase

import com.unimib.oases.di.IoDispatcher
import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.SexSpecificity
import com.unimib.oases.domain.repository.DiseaseRepository
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.ui.screen.nurse_assessment.demographics.Sex
import com.unimib.oases.ui.screen.nurse_assessment.history.PatientDiseaseState
import com.unimib.oases.util.AppConstants
import com.unimib.oases.util.firstSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetChronicDiseasesInfo  @Inject constructor(
    private val patientRepository: PatientRepository,
    private val diseaseRepository: DiseaseRepository,
    private val getPatientChronicDiseasesMapUseCase: GetPatientChronicDiseasesMapUseCase,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
){
    suspend operator fun invoke(patientId: String): List<PatientDiseaseState> {
        return withContext(ioDispatcher){
            val diseasesDeferred = async {
                val (sex, age) = getSexAndAge(patientId)
                diseaseRepository.getFilteredDiseases(sex, age).firstSuccess()
            }
            val patientDiseasesDeferred = async { getPatientChronicDiseasesMapUseCase(patientId) }
            val diseases = diseasesDeferred.await()
            val patientDiseases = patientDiseasesDeferred.await()
            return@withContext diseases.map { it.toState() }
                .map { diseaseState ->
                    patientDiseases[diseaseState.disease]?.let {
                        diseaseState.copy(
                            isDiagnosed = it.isDiagnosed,
                            additionalInfo = it.additionalInfo,
                            date = it.diagnosisDate,
                            freeTextValue = it.freeTextValue
                        )
                    } ?: diseaseState
                }
        }
    }

    private suspend fun getSexAndAge(patientId: String): Pair<SexSpecificity, AgeSpecificity> {
        return patientRepository.getPatientById(patientId).firstSuccess().let {
            it.sex.toSexSpecificity() to it.age.toAgeSpecificity()
        }
    }
    private fun Disease.toState() : PatientDiseaseState {
        return PatientDiseaseState(
            disease = name,
            entryType = entryType,
            group = group
        )
    }

    private fun Int.toAgeSpecificity(): AgeSpecificity {
        return if (this >= AppConstants.MATURITY_AGE) {
            AgeSpecificity.ADULTS
        } else{
            AgeSpecificity.CHILDREN
        }
    }

    private fun Sex.toSexSpecificity(): SexSpecificity {
        return when (this) {
            Sex.MALE -> SexSpecificity.MALE
            Sex.FEMALE -> SexSpecificity.FEMALE
        }
    }
}