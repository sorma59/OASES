package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class GetCurrentVisitMainComplaintUseCase @Inject constructor(
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val complaintSummaryRepository: ComplaintSummaryRepository
) {

    suspend operator fun invoke(patientId: String, visit: Visit? = null): Resource<List<ComplaintSummary>> {
        if (visit != null) { // If visit was passed as parameter, use it
            return try {
                val complaint = complaintSummaryRepository
                    .getVisitComplaintsSummaries(visit.id)
                    .firstSuccess()
                Resource.Success(complaint)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        } else { // Otherwise, get it from db
            return try {
                val visitFromDb = getCurrentVisitUseCase(patientId)
                val complaint = complaintSummaryRepository
                    .getVisitComplaintsSummaries(visitFromDb.id)
                    .firstSuccess()
                Resource.Success(complaint)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}