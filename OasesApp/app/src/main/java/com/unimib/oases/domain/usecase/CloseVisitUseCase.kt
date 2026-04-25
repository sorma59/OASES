package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Disposition
import com.unimib.oases.domain.model.DispositionType
import com.unimib.oases.domain.model.Ward
import com.unimib.oases.domain.repository.DispositionRepository
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.ui.screen.medical_visit.disposition.DispositionChoice
import com.unimib.oases.ui.screen.medical_visit.disposition.DispositionState
import com.unimib.oases.ui.screen.medical_visit.disposition.WardChoice
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class CloseVisitUseCase @Inject constructor(
    private val visitRepository: VisitRepository,
    private val dispositionRepository: DispositionRepository,
) {
    suspend operator fun invoke(state: DispositionState): Outcome<Unit> {
        when (val outcome = dispositionRepository.insertDisposition(state.toDomain())) {
            is Outcome.Error -> error(outcome.message)
            is Outcome.Success -> {
                return when (state.getDispositionType()) {
                    DispositionType.Discharge -> visitRepository.finalizeVisit(state.visitId, "DISMISSED")
                    is DispositionType.Hospitalization -> visitRepository.finalizeVisit(state.visitId, "HOSPITALIZED")
                }

            }
        }

    }

    private fun DispositionState.toDomain(): Disposition {
        return Disposition(
            visitId = this.visitId,
            dispositionType = this.getDispositionType(),
            dispositionTypeLabel = this.dispositionChoice.label,
            homeTreatments = this.homeTreatments,
            prescribedTherapiesText = this.prescribedTreatmentsText,
            finalDiagnosisText = this.finalDiagnosisText,
        )
    }

    private fun DispositionState.getDispositionType(): DispositionType {
        return when (this.dispositionChoice) {
            DispositionChoice.DISCHARGE -> DispositionType.Discharge
            DispositionChoice.HOSPITALIZE -> DispositionType.Hospitalization(this.wardChoice.toDomain())
            DispositionChoice.NONE -> error("Missing disposition choice")
        }
    }

    private fun WardChoice.toDomain(): Ward {
        return when (this) {
            WardChoice.MEDICAL -> Ward.MEDICAL
            WardChoice.SURGICAL -> Ward.SURGICAL
            WardChoice.CHILDREN -> Ward.CHILDREN
            WardChoice.MATERNITY -> Ward.MATERNITY
            WardChoice.HIGH_DEPENDENCY_UNIT -> Ward.HIGH_DEPENDENCY_UNIT
            WardChoice.NONE -> error("Missing ward choice")
        }
    }
}