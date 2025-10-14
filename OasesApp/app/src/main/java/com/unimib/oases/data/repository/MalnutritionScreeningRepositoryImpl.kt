package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.MalnutritionScreening
import com.unimib.oases.domain.repository.MalnutritionScreeningRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
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

    override fun getMalnutritionScreening(visitId: String): Flow<Resource<MalnutritionScreening?>> = flow {
//        if (Random.nextBoolean())
//            emit(Resource.Error("Mock error"))
//        else
            roomDataSource.getMalnutritionScreening(visitId)
                .onStart {
                    emit(Resource.Loading())
                }
                .catch { exception ->
                    Log.e(
                        "MalnutritionScreeningRepository",
                        "Error getting malnutrition screening for visitId $visitId: ${exception.message}",
                        exception
                    )
                    emit(Resource.Error(exception.message ?: "An error occurred"))
                }
                .collect { entity ->
                    val domainModel = entity?.toDomain()
                    emit(Resource.Success(domainModel))
                    // if entity is null then null is returned in Resource Success
                    // the reason is a visit can have no screening
                }
    }
}