package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface VisitRepository {

    suspend fun addVisit(visit: Visit): Outcome

    suspend fun addVisitWithTriageEvaluation(visit: Visit, triageEvaluation: TriageEvaluation, triageCode: TriageCode, room: Room): Outcome

    suspend fun updateVisit(visit: Visit): Outcome

    fun getVisits(patientId: String): Flow<Resource<List<Visit>>>

    fun getCurrentVisit(patientId: String): Flow<Resource<Visit>>
}