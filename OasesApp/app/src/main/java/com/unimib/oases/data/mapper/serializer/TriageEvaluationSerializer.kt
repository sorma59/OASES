package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.data.local.Converters.fromListToString
import com.unimib.oases.data.local.Converters.fromStringToList
import com.unimib.oases.domain.model.TriageEvaluation
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TriageEvaluationSerializer {
    fun serialize(triageEvaluation: TriageEvaluation): ByteArray {
        val visitIdBytes = triageEvaluation.visitId.toByteArray(Charsets.UTF_8)
        val redSymptomIdsBytes = fromListToString(triageEvaluation.redSymptomIds).toByteArray(Charsets.UTF_8)
        val yellowSymptomIdsBytes = fromListToString(triageEvaluation.yellowSymptomIds).toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + visitIdBytes.size +
            4 + redSymptomIdsBytes.size +
            4 + yellowSymptomIdsBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(visitIdBytes)

        buffer.putBytes(redSymptomIdsBytes)

        buffer.putBytes(yellowSymptomIdsBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): TriageEvaluation {
        val buffer = ByteBuffer.wrap(bytes)

        val visitId = buffer.readString()
        val redSymptomIds = buffer.readString()
        val yellowSymptomIds = buffer.readString()

        return TriageEvaluation(
            visitId = visitId,
            redSymptomIds = fromStringToList(redSymptomIds),
            yellowSymptomIds = fromStringToList(yellowSymptomIds)
        )
    }
}