package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.PatientWithVisitInfo
import com.unimib.oases.domain.model.TriageEvaluation
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class VisitRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
): VisitRepository {

    override suspend fun addVisit(visit: Visit): Outcome<Unit> {
        return try {
            roomDataSource.insertVisit(visit.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Log.e("VisitRepository", "Error adding visit: ${e.message}")
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun insertTriageEvaluationAndUpdateVisit(
        visit: Visit,
        triageEvaluation: TriageEvaluation
    ): Outcome<Unit> {
        return try {
            roomDataSource.insertTriageEvaluationAndUpdateVisit(visit.toEntity(), triageEvaluation.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Log.e("VisitRepository", "Error adding visit with triage evaluation: ${e.message}")
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateVisit(visit: Visit): Outcome<Unit> {
        return try {
            roomDataSource.upsertVisit(visit.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Log.e("VisitRepository", "Error updating visit: ${e.message}")
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun getVisits(patientId: String): Flow<Resource<List<Visit>>> = flow {
        roomDataSource.getVisits(patientId)
            .onStart { emit(Resource.Loading()) }
            .catch {
                Log.e("VisitRepository", "Error getting visits: ${it.message}")
                emit(Resource.Error(it.message ?: "An error occurred"))
            }
            .collect {
                emit(Resource.Success(it.map { entity -> entity.toDomain() }))
            }
    }

    override fun getVisitById(visitId: String): Flow<Resource<Visit>> = flow {
        roomDataSource.getVisitById(visitId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                Log.e("VisitRepository", "Error getting visit with id: $visitId")
                emit(Resource.Error(it.message ?: "An error occurred"))
            }
            .collect {
                emit(Resource.Success(it.toDomain()))
            }
    }

    override fun getVisitWithPatientInfo(visitId: String): Flow<Resource<PatientWithVisitInfo>> = flow {
        roomDataSource.getVisitWithPatientInfo(visitId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                Log.e("VisitRepository", "Error getting visit and patient info with visit's id: $visitId")
                emit(Resource.Error(it.message ?: "An error occurred"))
            }
            .collect {
                emit(Resource.Success(it.toDomain()))
            }
    }

    override fun getCurrentVisit(patientId: String): Flow<Resource<Visit>> = flow {
        roomDataSource.getCurrentVisit(patientId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch {
                Log.e("VisitRepository", "Error getting current visit: ${it.message}")
                emit(Resource.Error(it.message ?: "An error occurred"))
            }
            .collect { entity ->
                val resource = when (entity) {
                    null -> Resource.NotFound()
                    else -> Resource.Success(entity.toDomain())
                }
                emit(resource)
            }
    }
}