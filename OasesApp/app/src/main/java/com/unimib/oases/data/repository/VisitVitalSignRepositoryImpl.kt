package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.data.mapper.toVisitVitalSign
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VisitVitalSignRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
): VisitVitalSignRepository
{
    override suspend fun addVisitVitalSign(visitVitalSign: VisitVitalSign): Resource<Unit> {

        return try {
            roomDataSource.insertVisitVitalSigns(visitVitalSign.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }

    }

    override fun getVisitVitalSigns(visitId: String): Flow<Resource<List<VisitVitalSign>>>  = flow {

        try {
            emit(Resource.Loading())
            roomDataSource.getVisitVitalSigns(visitId).collect {
                emit(Resource.Success<List<VisitVitalSign>>(it.map { entity -> entity.toVisitVitalSign() }))
            }
        } catch (e: Exception) {
            Log.e("VisitVitalSignRepository", "Error getting visit vital signs: ${e.message}")
        }

    }

}