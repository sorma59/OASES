package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.complaint.Finding
import com.unimib.oases.domain.model.findingsById
import java.nio.ByteBuffer
import java.nio.ByteOrder

object FindingSerializer {
    fun serialize(finding: Finding): ByteArray {
        val idBytes = finding.id.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer
            .allocate(4 + idBytes.size)
            .order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(idBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Finding {
        val buffer = ByteBuffer.wrap(bytes)

        val idBytes = buffer.readString()

        return findingsById[idBytes]
            ?: throw IllegalArgumentException("Unknown finding ID: $idBytes")
    }
}