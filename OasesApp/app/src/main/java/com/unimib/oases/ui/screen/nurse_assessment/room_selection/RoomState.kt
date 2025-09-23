package com.unimib.oases.ui.screen.nurse_assessment.room_selection

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.graphics.Color
import com.unimib.oases.domain.model.Room
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.PatientVitalSignState

data class RoomState (
    val rooms: List<Room> = emptyList(),
    val currentRoom: Room? = null,
    val currentTriageCode: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)