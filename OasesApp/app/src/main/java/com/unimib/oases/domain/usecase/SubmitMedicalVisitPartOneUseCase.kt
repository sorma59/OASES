package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintState
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SubmitMedicalVisitPartOneUseCase @Inject constructor(
    private val buildComplaintSummaryFromStateUseCase: BuildComplaintSummaryFromStateUseCase,
    private val complaintSummaryRepository: ComplaintSummaryRepository
){

    suspend operator fun invoke(state: MainComplaintState): Outcome {
        return try {
            val complaintSummary = buildComplaintSummaryFromStateUseCase(state)
            complaintSummaryRepository.addComplaintSummary(complaintSummary)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

}