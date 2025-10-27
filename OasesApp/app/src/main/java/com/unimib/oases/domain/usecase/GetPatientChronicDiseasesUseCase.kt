package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientDisease
import com.unimib.oases.domain.repository.PatientDiseaseRepository
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class GetPatientChronicDiseasesUseCase @Inject constructor(
    val repo: PatientDiseaseRepository
){

    suspend operator fun invoke(patientId: String): List<PatientDisease> {
        return repo
            .getPatientDiseases(patientId)
            .firstSuccess()
    }

}