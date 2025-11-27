package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.VisitVitalSign
import java.nio.ByteBuffer
import java.nio.ByteOrder

object VisitVitalSignSerializer {

    fun serialize(visitVitalSign: VisitVitalSign): ByteArray {
        val visitIdBytes = visitVitalSign.visitId.toByteArray(Charsets.UTF_8)
        val vitalSignNameBytes = visitVitalSign.vitalSignName.toByteArray(Charsets.UTF_8)
        val timestampBytes = visitVitalSign.timestamp.toByteArray(Charsets.UTF_8)
        val valueBytes = visitVitalSign.value.toString().toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + visitIdBytes.size +
            4 + vitalSignNameBytes.size +
            4 + timestampBytes.size +
            4 + valueBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(visitIdBytes)

        buffer.putBytes(vitalSignNameBytes)

        buffer.putBytes(timestampBytes)

        buffer.putBytes(valueBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): VisitVitalSign {

        val buffer = ByteBuffer.wrap(bytes)

        val visitId = buffer.readString()
        val vitalSignName = buffer.readString()
        val timestamp = buffer.readString()
        val value = buffer.readString().toDouble()

        return VisitVitalSign(
            visitId = visitId,
            vitalSignName = vitalSignName,
            timestamp = timestamp,
            value = value
        )
    }

    // ----------------Testing--------------------
    fun testVisitVitalSignSerializer() {
        val original = VisitVitalSign(
            visitId = "id",
            vitalSignName = "vital sign",
            timestamp = "timestamp",
            value = 5.0
        )
        Log.d("Prova: VisitVitalSignSerializer", "Original: $original")
        val bytes = serialize(original)
        val recovered = deserialize(bytes)
        Log.d("Prova: VisitVitalSignSerializer", "Recovered: $recovered")
    }
}