package com.unimib.oases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.unimib.oases.data.local.TableNames
import com.unimib.oases.data.local.model.RoomEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RoomsDao {
    @Upsert
    suspend fun insert(room: RoomEntity)

    @Delete
    suspend fun delete(room: RoomEntity)

    @Query("SELECT * FROM " + TableNames.ROOMS + " WHERE name = :name")
    fun getRoom(name: String): Flow<RoomEntity?>

    @Query("SELECT * FROM " + TableNames.ROOMS)
    fun getAllRooms(): Flow<List<RoomEntity>>
}