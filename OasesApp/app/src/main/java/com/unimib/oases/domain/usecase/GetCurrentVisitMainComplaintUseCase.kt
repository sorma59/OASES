package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.util.Resource
import com.unimib.oases.util.firstNullableSuccess
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
            return try {
                val visitFromDb = getCurrentVisitUseCase(patientId).firstNullableSuccess()
                visitFromDb?.let {
                    val complaints =
                        complaintSummaryRepository.getVisitComplaintsSummaries(visitFromDb.id).first {
                            it is Resource.Success || it is Resource.Error
                        }
                    complaints
                } ?: Resource.Error("No current visit")
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}