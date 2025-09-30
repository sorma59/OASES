package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.ComplaintSummary
import com.unimib.oases.domain.repository.ComplaintSummaryRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ComplaintSummaryRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): ComplaintSummaryRepository {

    override suspend fun addComplaintSummary(complaintSummary: ComplaintSummary): Resource<Unit> {
        return try {
            roomDataSource.insertComplaintSummary(complaintSummary.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteComplaintSummary(complaintSummary: ComplaintSummary): Resource<Unit> {
        return try {
            roomDataSource.deleteComplaintSummary(complaintSummary.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun getVisitComplaintsSummaries(visitId: String): Flow<Resource<List<ComplaintSummary>>> = flow {
        roomDataSource.getVisitComplaintsSummaries(visitId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "ComplaintSummaryRepository",
                    "Error getting complaint summaries for visitId $visitId: ${exception.message}",
                    exception
                )
            }
            .collect { entities ->
                val domainModels = entities.map { it.toDomain() }
                emit(Resource.Success(domainModels))
            }
    }

    override fun getComplaintSummary(visitId: String, complaintId: String): Flow<Resource<ComplaintSummary?>> = flow {
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
                val domainModel = entity?.toDomain()
                emit(Resource.Success(domainModel))
            }
    }

}