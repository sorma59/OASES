package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCurrentVisitMainComplaintUseCase @Inject constructor(
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase,
    private val complaintSummaryRepository: ComplaintSummaryRepository
) {

    suspend operator fun invoke(patientId: String, visit: Visit? = null): Resource<List<ComplaintSummary>> {
        visit?.let { // If visit was passed as parameter, use it
            return try {
                val complaints = complaintSummaryRepository.getVisitComplaintsSummaries(visit.id).first {
                    it is Resource.Success || it is Resource.Error
                }
                complaints
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        } ?: run { // Otherwise, get it from db
            val visitResource = getCurrentVisitUseCase(patientId)
            if (visitResource is Resource.Error)
                return Resource.Error(visitResource.message ?: "Error during the retrieval of the patient's current visit")
            val visit = visitResource.data
            visit?.let{
                return try {
                    val complaints = complaintSummaryRepository.getVisitComplaintsSummaries(visit.id).first {
                        it is Resource.Success || it is Resource.Error
                    }
                    complaints
                } catch (e: Exception) {
                    Resource.Error(e.message ?: "Unknown error")
                }
            }
            return Resource.Error("No current visit")
        }
    }
}