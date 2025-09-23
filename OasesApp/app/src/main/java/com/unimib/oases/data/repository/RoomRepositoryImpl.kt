package com.unimib.oases.data.repository

import android.util.Log
import com.unimib.oases.data.local.RoomDataSource
import com.unimib.oases.data.mapper.toDomain
import com.unimib.oases.data.mapper.toEntity
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.repository.RoomRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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

    override fun deleteRoom(name: String): Resource<Unit> {
        return try {
            //launch a coroutine to run the suspend function
            CoroutineScope(Dispatchers.IO).launch {
                roomDataSource.getRoom(name).firstOrNull()?.let {
                    roomDataSource.deleteRoom(it)
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getAllRooms(): Flow<Resource<List<Room>>> = flow {
        emit(Resource.Loading())
        roomDataSource.getAllRooms().collect {
            emit(Resource.Success(it.map { entity -> entity.toDomain() }))
        }
    }


}