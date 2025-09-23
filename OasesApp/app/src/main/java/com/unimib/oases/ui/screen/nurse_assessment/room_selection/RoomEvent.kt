package com.unimib.oases.ui.screen.nurse_assessment.room_selection

import com.unimib.oases.domain.model.Room

sealed class RoomEvent {
    data class RoomSelected(val room: Room): RoomEvent()
    data object ConfirmSelection : RoomEvent()
    data object RoomDeselected: RoomEvent()
    data object Retry: RoomEvent()

}