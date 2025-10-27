package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface ComplaintSummaryRepository {
    suspend fun addComplaintSummary(complaintSummary: ComplaintSummary): Outcome
    suspend fun addComplaintSummaries(complaintSummaries: List<ComplaintSummary>): Outcome
    suspend fun deleteComplaintSummary(complaintSummary: ComplaintSummary): Outcome
    fun getVisitComplaintsSummaries(visitId: String): Flow<Resource<List<ComplaintSummary>>>
    fun getComplaintSummary(visitId: String, complaintId: String): Flow<Resource<ComplaintSummary>>
}