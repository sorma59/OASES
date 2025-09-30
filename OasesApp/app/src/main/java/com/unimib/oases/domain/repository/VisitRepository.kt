package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Visit
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface VisitRepository {

    suspend fun addVisit(visit: Visit): Resource<Unit>

    suspend fun updateVisit(visit: Visit): Resource<Unit>

    fun getVisits(patientId: String): Flow<Resource<List<Visit>>>

    suspend fun getCurrentVisit(patientId: String): Visit?
}