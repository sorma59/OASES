package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.VisitRepository
import javax.inject.Inject

class GetCurrentVisitUseCase @Inject constructor(
    private val visitRepository: VisitRepository
) {
    operator fun invoke(patientId: String): Visit? {
        return visitRepository.getCurrentVisit(patientId)
    }
}