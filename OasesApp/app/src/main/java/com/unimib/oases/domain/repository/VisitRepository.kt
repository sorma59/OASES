package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface VisitRepository {

    suspend fun addVisit(visit: Visit): Outcome<Unit>

    suspend fun insertTriageEvaluationAndUpdateVisit(visit: Visit, triageEvaluation: TriageEvaluation): Outcome<Unit>

    suspend fun updateVisit(visit: Visit): Outcome<Unit>

    fun getVisits(patientId: String): Flow<Resource<List<Visit>>>

    fun getVisitById(visitId: String): Flow<Resource<Visit>>

    fun getVisitWithPatientInfo(visitId: String): Flow<Resource<PatientWithVisitInfo>>

    fun getCurrentVisit(patientId: String): Flow<Resource<Visit>>
}