package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.QuestionAndAnswer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object QuestionAndAnswerSerializer {

    fun serialize(questionAndAnswer: QuestionAndAnswer): ByteArray {
        val questionBytes = questionAndAnswer.question.toByteArray(Charsets.UTF_8)
        val answerBytes = questionAndAnswer.answer.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + questionBytes.size +
                    4 + answerBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(questionBytes)

        buffer.putBytes(answerBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): QuestionAndAnswer {
        val buffer = ByteBuffer.wrap(bytes)

        val question = buffer.readString()
        val answer = buffer.readString()

        return QuestionAndAnswer(question, answer)
    }

}