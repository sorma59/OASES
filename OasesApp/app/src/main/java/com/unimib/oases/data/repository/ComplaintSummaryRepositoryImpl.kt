package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.local.model.ComplaintSummaryEntity
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ComplaintSummaryRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): ComplaintSummaryRepository {

    override suspend fun addComplaintSummary(complaintSummary: ComplaintSummary): Outcome {
        return try {
            roomDataSource.insertComplaintSummary(complaintSummary.toEntity())
            Outcome.Success
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteComplaintSummary(complaintSummary: ComplaintSummary): Outcome {
        return try {
            roomDataSource.deleteComplaintSummary(complaintSummary.toEntity())
            Outcome.Success
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun getVisitComplaintsSummaries(visitId: String): Flow<Resource<List<ComplaintSummary>>> =
        roomDataSource.getVisitComplaintsSummaries(visitId)
            .map<List<ComplaintSummaryEntity>, Resource<List<ComplaintSummary>>> { entities ->
                val domainModels = entities.map { it.toDomain() }
                Resource.Success(domainModels)
            }
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                val message = "Error getting complaint summaries for visitId $visitId: ${exception.message}"
                Log.e("ComplaintSummaryRepository", message, exception)
                emit(Resource.Error(message))
            }

    override fun getComplaintSummary(visitId: String, complaintId: String): Flow<Resource<ComplaintSummary>> = flow {
        roomDataSource.getComplaintSummary(visitId, complaintId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "ComplaintSummaryRepository",
                    "Error getting complaint summary for visitId $visitId and complaintId $complaintId: ${exception.message}",
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