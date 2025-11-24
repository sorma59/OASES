package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface ComplaintSummaryRepository {
    suspend fun addComplaintSummary(complaintSummary: ComplaintSummary): Outcome<Unit>
    suspend fun addComplaintSummaries(complaintSummaries: List<ComplaintSummary>): Outcome<Unit>
    suspend fun deleteComplaintSummary(complaintSummary: ComplaintSummary): Outcome<Unit>
    fun getVisitComplaintsSummaries(visitId: String): Flow<Resource<List<ComplaintSummary>>>
    fun getComplaintSummary(visitId: String, complaintId: String): Flow<Resource<ComplaintSummary>>
}