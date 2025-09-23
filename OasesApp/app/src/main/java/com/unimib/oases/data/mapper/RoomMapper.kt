package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.RoomEntity
import com.unimib.oases.data.local.model.TriageEvaluationEntity
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.TriageEvaluation

fun Room.toEntity(): RoomEntity {
    return RoomEntity(
        name = name,
    )
}

fun RoomEntity.toDomain(): Room {
    return Room(
        name = name,
    )
}