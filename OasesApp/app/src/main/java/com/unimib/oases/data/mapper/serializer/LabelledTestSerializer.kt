package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.complaint.LabelledTest
import java.nio.ByteBuffer
import java.nio.ByteOrder

object LabelledTestSerializer {

    fun serialize(labelledTest: LabelledTest): ByteArray {
        val testIdBytes = labelledTest.testId.toByteArray()
        val labelBytes = labelledTest.label.toByteArray()

        val buffer = ByteBuffer
            .allocate(
                4 + testIdBytes.size +
                4 + labelBytes.size
            )
            .order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(testIdBytes.size)
        buffer.put(testIdBytes)

        buffer.putInt(labelBytes.size)
        buffer.put(labelBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): LabelledTest {
        val buffer = ByteBuffer.wrap(bytes)

        val testId = buffer.readString()
        val label = buffer.readString()

        return LabelledTest(testId, label)
    }

}