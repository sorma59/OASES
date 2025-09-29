package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ImmediateTreatmentSerializer {

    fun serialize(immediateTreatment: ImmediateTreatment): ByteArray {
        val textBytes = immediateTreatment.text.toByteArray()

        val buffer = ByteBuffer
            .allocate(4 + textBytes.size)
            .order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(textBytes.size)
        buffer.put(textBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): ImmediateTreatment {
        val buffer = ByteBuffer.wrap(bytes)

        val text = buffer.readString()

        return ImmediateTreatment(text)
    }

}