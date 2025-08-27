package com.unimib.oases.data.repository

import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.util.Resource
import javax.inject.Inject

class MalnutritionScreeningRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
): MalnutritionScreeningRepository {

    override suspend fun insertMalnutritionScreening(malnutritionScreening: MalnutritionScreening): Resource<Unit> {
        return try {
            roomDataSource.insertMalnutritionScreening(malnutritionScreening.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override fun getMalnutritionScreening(visitId: String): Resource<MalnutritionScreening?> {
        return try {
            val entity = roomDataSource.getMalnutritionScreening(visitId)
            Resource.Success(entity?.toDomain())
            // if entity is null then null is returned in Resource Success
            // the reason is a visit can have no screening
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}