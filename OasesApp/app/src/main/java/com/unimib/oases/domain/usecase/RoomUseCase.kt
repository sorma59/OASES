package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.repository.RoomRepository
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomUseCase @Inject constructor(
    private val repo: RoomRepository
) {
    suspend fun addRoom(room: Room): Outcome<Unit> {
        return repo.addRoom(room)
    }

    suspend fun deleteRoom(room: Room): Outcome<Unit> {
        return repo.deleteRoom(room)
    }

    fun getRooms(): Flow<Resource<List<Room>>> {
        return repo.getAllRooms()
    }
}