package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class GetLatestVitalSignsUseCase @Inject constructor(
    private val visitVitalSignRepository: VisitVitalSignRepository
) {
    suspend operator fun invoke(patientId: String): List<VisitVitalSign>{
        return visitVitalSignRepository
            .getVisitLatestVitalSigns(patientId)
            .firstSuccess()
    }
}