package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.util.Outcome
import jakarta.inject.Inject

class VisitUseCase @Inject constructor(
    private val visitRepository: VisitRepository
) {
    suspend fun addVisit(visit: Visit): Outcome {
        return visitRepository.addVisit(visit)
    }
}