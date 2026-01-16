package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.ui.screen.nurse_assessment.history.VisitState
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class ReloadPastVisitsUseCase @Inject constructor(
    private val repo: VisitRepository
){
    suspend operator fun invoke(patientId: String): List<VisitState> {
        return repo
            .getVisits(patientId)
            .firstSuccess()
            .toVisitStates()
    }

    private fun List<Visit>.toVisitStates(): List<VisitState> {
        return this.map {
            VisitState(
                visitId = it.id,
                date = it.date.toString(),
                triageCode = it.triageCode.getColor(),
                status = it.patientStatus
            )
        }
    }
}