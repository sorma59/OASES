package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.complaint.SupportiveTherapyText
import java.nio.ByteBuffer
import java.nio.ByteOrder

object SupportiveTherapyTextSerializer {

    fun serialize(supportiveTherapyText: SupportiveTherapyText): ByteArray {
        val textBytes = supportiveTherapyText.text.toByteArray()

        val buffer = ByteBuffer
            .allocate(4 + textBytes.size)
            .order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(textBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): SupportiveTherapyText {
        val buffer = ByteBuffer.wrap(bytes)

        val text = buffer.readString()

        return SupportiveTherapyText(text)
    }

}