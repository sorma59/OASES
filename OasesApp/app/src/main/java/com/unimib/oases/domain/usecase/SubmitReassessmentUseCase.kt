package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Reassessment
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.repository.ReassessmentRepository
import com.unimib.oases.ui.screen.medical_visit.reassessment.ReassessmentState
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SubmitReassessmentUseCase @Inject constructor(
    private val reassessmentRepository: ReassessmentRepository,
) {

    suspend operator fun invoke(state: ReassessmentState): Outcome<Unit> {
        return reassessmentRepository.insert(state.toDomain())
    }

    private fun ReassessmentState.toDomain(): Reassessment {
        checkNotNull(definitiveTherapies) {
            "Definitive therapies must be generated before submitting"
        }
        return Reassessment(
            visitId = visitId,
            complaintId = ComplaintId.complaints[complaintId] ?: error("Unknown complaintId $complaintId"),
            symptoms = symptoms,
            findings = findings,
            definitiveTherapies = definitiveTherapies.map { it.description }.toSet(),
        )
    }
}