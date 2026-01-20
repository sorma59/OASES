package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.domain.util.TimeProvider
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class CreateReturnVisitUseCase @Inject constructor(
    private val visitRepository: VisitRepository,
    private val timeProvider: TimeProvider
) {
    suspend operator fun invoke(patientId: String): Outcome<String> {
        return visitRepository.addVisit(
            Visit(
                patientId = patientId,
                arrivalTime = timeProvider.nowTime(),
                date = timeProvider.today()
            )
        )
    }
}