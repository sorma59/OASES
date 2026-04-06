package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.complaint.TherapyText
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TherapyTextSerializer {

    fun serialize(therapyText: TherapyText): ByteArray {
        val textBytes = therapyText.text.toByteArray()

        val buffer = ByteBuffer
            .allocate(4 + textBytes.size)
            .order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(textBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): TherapyText {
        val buffer = ByteBuffer.wrap(bytes)

        val text = buffer.readString()

        return TherapyText(text)
    }

}