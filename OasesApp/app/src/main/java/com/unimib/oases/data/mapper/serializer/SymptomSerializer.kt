package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.symptom.Symptom
import java.nio.ByteBuffer
import java.nio.ByteOrder

object SymptomSerializer {
    fun serialize(symptom: Symptom): ByteArray {
        val idBytes = symptom.id.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer
            .allocate(4 + idBytes.size)
            .order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(idBytes.size)
        buffer.put(idBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Symptom {
        val buffer = ByteBuffer.wrap(bytes)

        val idBytes = buffer.readString()

        return Symptom.symptoms[idBytes.toString()]
            ?: throw IllegalArgumentException("Unknown symptom ID: $idBytes")
    }
}