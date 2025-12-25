package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientDisease
import javax.inject.Inject

class GetPatientChronicDiseasesMapUseCase @Inject constructor(
    private val getPatientChronicDiseasesUseCase: GetPatientChronicDiseasesUseCase
) {

    suspend operator fun invoke(patientId: String): Map<String, PatientDisease> {
        return getPatientChronicDiseasesUseCase(patientId).associateBy {
            it.diseaseName
        }
    }

}