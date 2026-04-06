package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Reassessment
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface ReassessmentRepository {
    suspend fun insert(reassessment: Reassessment): Outcome<Unit>
    fun getVisitReassessments(visitId: String): Flow<Resource<List<Reassessment>>>
    fun getReassessment(visitId: String, complaintId: String): Flow<Resource<Reassessment>>
}