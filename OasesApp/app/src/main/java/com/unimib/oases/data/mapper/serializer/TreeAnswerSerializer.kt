package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.data.local.model.TreeAnswers
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TreeAnswersSerializer {

    fun serialize(treeAnswers: TreeAnswers): ByteArray {
        val treeIdBytes = treeAnswers.treeId.toByteArray(Charsets.UTF_8)
        val pathBytes = treeAnswers.path.map { if (it) 1.toByte() else 0.toByte() }.toByteArray()

        val buffer = ByteBuffer.allocate(
            4 + treeIdBytes.size +
                    4 + pathBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(treeIdBytes)
        buffer.putInt(pathBytes.size)
        buffer.put(pathBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): TreeAnswers {
        val buffer = ByteBuffer.wrap(bytes)

        val treeId = buffer.readString()
        val pathSize = buffer.int
        val pathBytes = ByteArray(pathSize).also { buffer.get(it) }

        return TreeAnswers(
            treeId = treeId,
            path = pathBytes.map { it == 1.toByte() }
        )
    }
}