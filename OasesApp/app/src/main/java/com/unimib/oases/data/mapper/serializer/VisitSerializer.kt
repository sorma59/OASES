package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.TriageCode.Companion.fromTriageCodeName
import com.unimib.oases.domain.model.Visit
import java.nio.ByteBuffer

object VisitSerializer {

    fun serialize(visit: Visit): ByteArray {
        val idBytes = visit.id.toByteArray(Charsets.UTF_8)
        val patientIdBytes = visit.patientId.toByteArray(Charsets.UTF_8)
        val triageCodeBytes = visit.triageCode.name.toByteArray(Charsets.UTF_8)
        val dateBytes = visit.date.toByteArray(Charsets.UTF_8)
        val descriptionBytes = visit.description.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + idBytes.size +
            4 + patientIdBytes.size +
            4 + triageCodeBytes.size +
            4 + dateBytes.size +
            4 + descriptionBytes.size
        )

        buffer.putInt(idBytes.size)
        buffer.put(idBytes)

        buffer.putInt(patientIdBytes.size)
        buffer.put(patientIdBytes)

        buffer.putInt(triageCodeBytes.size)
        buffer.put(triageCodeBytes)

        buffer.putInt(dateBytes.size)
        buffer.put(dateBytes)

        buffer.putInt(descriptionBytes.size)
        buffer.put(descriptionBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Visit {
        val buffer = ByteBuffer.wrap(bytes)

        val id = buffer.readString()
        val patientId = buffer.readString()
        val triageCode = buffer.readString()
        val date = buffer.readString()
        val description = buffer.readString()

        return Visit(
            id = id,
            patientId = patientId,
            triageCode = fromTriageCodeName(triageCode),
            date = date,
            description = description
        )
    }

    // ----------------Testing--------------------
    fun test() {
        val original = Visit(
            id = "id",
            patientId = "patientId",
            triageCode = TriageCode.GREEN,
            date = "date",
            description = "description"
        )
        Log.d("VisitSerializer", "Original: $original")
        val bytes = serialize(original)
        val recovered = deserialize(bytes)
        Log.d("VisitSerializer", "Recovered: $recovered")
    }

}