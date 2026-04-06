package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.VisitRepository
import javax.inject.Inject

class CloseVisitUseCase @Inject constructor(
    private val visitRepository: VisitRepository,
) {
    suspend operator fun invoke(visitId: String) {

    }
}