package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.data.local.model.DetailQuestionAnswer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object DetailQuestionAnswerSerializer {

    fun serialize(detailQuestionAnswer: DetailQuestionAnswer): ByteArray {
        val questionBytes = detailQuestionAnswer.question.toByteArray(Charsets.UTF_8)
        val answerSymptomIdsBytes = detailQuestionAnswer.answerSymptomIds.map { it.toByteArray(Charsets.UTF_8) }

        val buffer = ByteBuffer.allocate(
            4 + questionBytes.size +
                    4 + answerSymptomIdsBytes.sumOf { 4 + it.size }
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(questionBytes)
        buffer.putInt(answerSymptomIdsBytes.size)
        answerSymptomIdsBytes.forEach { buffer.putBytes(it) }

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): DetailQuestionAnswer {
        val buffer = ByteBuffer.wrap(bytes)

        val question = buffer.readString()
        val answerCount = buffer.int
        val answerSymptomIds = List(answerCount) { buffer.readString() }

        return DetailQuestionAnswer(
            question = question,
            answerSymptomIds = answerSymptomIds
        )
    }
}