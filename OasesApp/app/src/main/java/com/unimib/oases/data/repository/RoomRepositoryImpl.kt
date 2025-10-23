package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.repository.RoomRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    val roomDataSource: RoomDataSource,
): RoomRepository {
    override suspend fun addRoom(room: Room): Resource<Unit> {
        return try {
            roomDataSource.insertRoom(room.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("RoomRepository", "Error adding the room: ${e.message}")
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun deleteRoom(room: Room): Resource<Unit> {
        return try {
            roomDataSource.deleteRoom(room.toEntity())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getAllRooms(): Flow<Resource<List<Room>>> = flow {
//        if (Random.nextBoolean())
//            emit(Resource.Error("Mock error"))
//        else
            roomDataSource.getAllRooms()
                .onStart { emit(Resource.Loading()) }
                .catch {
                    emit(Resource.Error(it.message ?: "Unknown error"))
                }
                .collect {
                    emit(
                        Resource.Success(
                            it.map { entity -> entity.toDomain() }
                        )
                    )
                }
    }

}