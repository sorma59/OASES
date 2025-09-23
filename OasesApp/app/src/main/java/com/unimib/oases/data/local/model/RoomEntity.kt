package com.unimib.oases.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unimib.oases.data.local.TableNames

@Entity(tableName = TableNames.ROOMS)
data class RoomEntity (
    @PrimaryKey
    @ColumnInfo(name = "name") val name: String,
)

