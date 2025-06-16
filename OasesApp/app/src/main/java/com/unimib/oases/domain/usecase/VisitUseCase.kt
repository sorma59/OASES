package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.util.Resource
import jakarta.inject.Inject

class VisitUseCase @Inject constructor(
    private val visitRepository: VisitRepository
) {
    suspend fun addVisit(visit: Visit): Resource<Unit> {
        return visitRepository.addVisit(visit)
    }
}