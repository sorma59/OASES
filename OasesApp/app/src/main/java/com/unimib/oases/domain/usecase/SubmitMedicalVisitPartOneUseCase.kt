package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintState
import com.unimib.oases.util.Resource
import javax.inject.Inject

class SubmitMedicalVisitPartOneUseCase @Inject constructor(
    private val buildComplaintSummaryFromStateUseCase: BuildComplaintSummaryFromStateUseCase,
    private val complaintSummaryRepository: ComplaintSummaryRepository
){

    suspend operator fun invoke(state: MainComplaintState): Resource<Unit> {
        return try {
            val complaintSummary = buildComplaintSummaryFromStateUseCase(state)
            complaintSummaryRepository.addComplaintSummary(complaintSummary)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}