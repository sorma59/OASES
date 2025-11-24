package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntities
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class VisitVitalSignRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
): VisitVitalSignRepository
{
    override suspend fun addVisitVitalSign(visitVitalSign: VisitVitalSign): Outcome<Unit> {
        return try {
            roomDataSource.insertVisitVitalSign(visitVitalSign.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addVisitVitalSigns(visitVitalSigns: List<VisitVitalSign>): Outcome<Unit> {
        return try {
            roomDataSource.insertVisitVitalSigns(visitVitalSigns.toEntities())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Log.e("VisitVitalSignRepository", "Error adding vital signs to visit: ${e.message}")
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun getVisitVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSign>>> = flow {
        roomDataSource.getVisitVitalSigns(visitId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "VisitVitalSignRepository",
                    "Error getting visit vital signs for visitId $visitId: ${exception.message}",
                    exception
                )
                emit(Resource.Error(exception.message ?: "An error occurred"))
            }
            .collect { entities ->
                val domainModels = entities.map { it.toDomain() }
                emit(Resource.Success(domainModels))
            }
    }

    override fun getVisitLatestVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSign>>> = flow {
        roomDataSource.getLatestVisitVitalSigns(visitId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "VisitVitalSignRepository",
                    "Error getting visit latest vital signs for visitId $visitId: ${exception.message}",
                    exception
                )
                emit(Resource.Error(exception.message ?: "An error occurred"))
            }
            .collect { entities ->
                val domainModels = entities.map { it.toDomain() }
                emit(Resource.Success(domainModels))
            }
    }
}