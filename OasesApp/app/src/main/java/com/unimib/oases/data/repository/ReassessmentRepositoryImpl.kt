package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.ReassessmentEntity
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Reassessment
import com.unimib.oases.domain.repository.ReassessmentRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ReassessmentRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): ReassessmentRepository {
    override suspend fun insert(reassessment: Reassessment): Outcome<Unit> {
        return try {
            roomDataSource.insertReassessment(reassessment.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Log.e("ReassessmentRepository", "Error adding reassessment: ${e.message}")
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun getVisitReassessments(visitId: String): Flow<Resource<List<Reassessment>>> =
        roomDataSource.getVisitReassessments(visitId)
            .map<List<ReassessmentEntity>, Resource<List<Reassessment>>> { entities ->
                val domainModels = entities.map { it.toDomain() }
                Resource.Success(domainModels)
            }
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                val message = "Error getting reassessments for visitId $visitId: ${exception.message}"
                Log.e("ReassessmentRepository", message, exception)
                emit(Resource.Error(message))
            }

    override fun getReassessment(visitId: String, complaintId: String): Flow<Resource<Reassessment>> = flow {
        roomDataSource.getReassessment(visitId, complaintId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "ReassessmentRepository",
                    "Error getting reassessment for visitId $visitId and complaintId $complaintId: ${exception.message}",
                    exception
                )
                emit(Resource.Error(exception.message ?: "An error occurred"))
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