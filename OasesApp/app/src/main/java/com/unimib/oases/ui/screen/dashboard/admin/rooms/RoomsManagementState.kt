package com.unimib.oases.ui.screen.dashboard.admin.rooms

import com.unimib.oases.domain.model.Room


data class RoomManagementState(
    val room: Room = Room(
        name = "",
    ),
    val rooms: List<Room> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

