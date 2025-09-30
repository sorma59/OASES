package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.VisitRepository
import javax.inject.Inject

class GetCurrentVisitUseCase @Inject constructor(
    private val visitRepository: VisitRepository
) {
    suspend operator fun invoke(patientId: String) = visitRepository.getCurrentVisit(patientId)
}