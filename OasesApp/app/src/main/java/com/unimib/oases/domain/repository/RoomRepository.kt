package com.unimib.oases.domain.repository

import com.unimib.oases.domain.model.Room
import com.unimib.oases.util.Outcome
import com.unimib.oases.util.Resource
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun addRoom(room: Room): Outcome
    suspend fun deleteRoom(room: Room): Outcome
    fun getAllRooms(): Flow<Resource<List<Room>>>
}