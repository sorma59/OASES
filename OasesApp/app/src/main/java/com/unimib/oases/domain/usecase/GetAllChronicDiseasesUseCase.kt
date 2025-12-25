package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.DiseaseRepository
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class GetAllChronicDiseasesUseCase @Inject constructor(
    private val diseaseRepository: DiseaseRepository
) {
    suspend operator fun invoke() = diseaseRepository.getAllDiseases().firstSuccess()
}