package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.util.DateAndTimeUtils
import java.nio.ByteBuffer
import java.time.LocalDate
import java.time.LocalTime

object VisitSerializer {

    fun serialize(visit: Visit): ByteArray {
        val idBytes = visit.id.toByteArray(Charsets.UTF_8)
        val patientIdBytes = visit.patientId.toByteArray(Charsets.UTF_8)
        val triageCodeBytes = visit.triageCode.name.toByteArray(Charsets.UTF_8)
        val patientStatusBytes = visit.patientStatus.name.toByteArray(Charsets.UTF_8)
        val roomNameBytes = visit.roomName?.toByteArray()
        val arrivalTimeBytes = visit.arrivalTime.toString().toByteArray()
        val dateBytes = visit.date.toString().toByteArray(Charsets.UTF_8)
        val descriptionBytes = visit.description.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + idBytes.size +
            4 + patientIdBytes.size +
            4 + triageCodeBytes.size +
            4 + patientStatusBytes.size +
            1 + (roomNameBytes?.let { 4 + it.size } ?: 0) +
            4 + arrivalTimeBytes.size +
            4 + dateBytes.size +
            4 + descriptionBytes.size
        )

        buffer.putData(idBytes)

        buffer.putData(patientIdBytes)

        buffer.putData(triageCodeBytes)

        buffer.putData(patientStatusBytes)

        buffer.putNullableBytes(roomNameBytes)

        buffer.putData(arrivalTimeBytes)

        buffer.putData(dateBytes)

        buffer.putData(descriptionBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Visit {
        val buffer = ByteBuffer.wrap(bytes)

        val id = buffer.readString()
        val patientId = buffer.readString()
        val triageCode = buffer.readString()
        val patientStatus = buffer.readString()
        val roomName = buffer.readNullableString()
        val arrivalTime = buffer.readString()
        val date = buffer.readString()
        val description = buffer.readString()

        return Visit(
            id = id,
            patientId = patientId,
            triageCode = TriageCode.valueOf(triageCode),
            patientStatus = PatientStatus.valueOf(patientStatus),
            roomName = roomName,
            arrivalTime = LocalTime.parse(arrivalTime),
            date = LocalDate.parse(date),
            description = description
        )
    }

    // ----------------Testing--------------------
    fun testVisitSerializer() {
        val original = Visit(
            id = "id",
            patientId = "patientId",
            triageCode = TriageCode.GREEN,
            roomName = "aaaa",
            date = DateAndTimeUtils.getCurrentDate(),
            description = "description"
        )
        Log.d("VisitSerializer", "Original: $original")
        val bytes = serialize(original)
        val recovered = deserialize(bytes)
        Log.d("VisitSerializer", "Recovered: $recovered")
    }

}