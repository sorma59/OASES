package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.repository.RoomRepository
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomUseCase @Inject constructor(
    private val repo: RoomRepository
) {
    suspend fun addRoom(room: Room){
        repo.addRoom(room)
    }

    fun getRooms(): Flow<Resource<List<Room>>> {
        val result = repo.getAllRooms()
        return result
    }

    fun deleteRoom(room: Room){
        repo.deleteRoom(room.name)
    }
}