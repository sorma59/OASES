package com.unimib.oases.ui.screen.dashboard.admin.rooms

import com.unimib.oases.domain.model.Room

sealed class RoomsManagementEvent {
    data class EnteredRoomName(val value: String) : RoomsManagementEvent()
    data class Delete(val value: Room) : RoomsManagementEvent()
    data class Click(val value: Room) : RoomsManagementEvent()
    data object SaveRoom : RoomsManagementEvent()
    data object UndoDelete: RoomsManagementEvent()
}