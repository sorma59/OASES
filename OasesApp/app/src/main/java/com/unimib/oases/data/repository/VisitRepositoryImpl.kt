package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.data.mapper.toVisit
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.VisitRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VisitRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
): VisitRepository {

    override suspend fun addVisit(visit: Visit): Resource<Unit> {
        return try {
            roomDataSource.insertVisit(visit.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("VisitRepository", "Error adding visit: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun updateVisit(visit: Visit): Resource<Unit> {
        return try {
            roomDataSource.upsertVisit(visit.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("VisitRepository", "Error updating visit: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun getVisits(patientId: String): Flow<Resource<List<Visit>>> = flow {

        try {
            emit(Resource.Loading())
            roomDataSource.getVisits(patientId).collect {
                emit(Resource.Success(it.map { entity -> entity.toVisit() }))
            }
        } catch (e: Exception) {
            Log.e("VisitRepository", "Error getting visits: ${e.message}")
            emit(Resource.Error(e.message ?: "An error occurred"))
        }

    }

    override fun getCurrentVisit(patientId: String): Visit? {
        return try {
            val visit = roomDataSource.getCurrentVisit(patientId)
            visit?.toVisit()
        } catch (e: Exception) {
            Log.e("VisitRepository", "Error getting visits: ${e.message}")
            null
        }
    }

}