package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Disposition
import com.unimib.oases.domain.repository.DispositionRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class DispositionRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
): DispositionRepository {
    override suspend fun insertDisposition(disposition: Disposition): Outcome<Unit> {
        return try {
            roomDataSource.insertDisposition(disposition.toEntity())
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Error(e.message ?: "An error occurred")
        }
    }

    override fun getDisposition(visitId: String): Flow<Resource<Disposition>> = flow {
        roomDataSource.getDisposition(visitId)
            .onStart {
                emit(Resource.Loading())
            }
            .catch { exception ->
                Log.e(
                    "DispositionRepository",
                    "Error getting disposition for visitId $visitId: ${exception.message}",
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