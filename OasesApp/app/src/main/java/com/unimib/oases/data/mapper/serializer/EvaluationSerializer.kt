package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.Evaluation
import com.unimib.oases.domain.model.complaint.ComplaintId
import java.nio.ByteBuffer

object EvaluationSerializer {

    fun serialize(evaluation: Evaluation): ByteArray {
        val visitIdBytes = evaluation.visitId.toByteArray(Charsets.UTF_8)
        val complaintIdBytes = evaluation.complaintId.id.toByteArray(Charsets.UTF_8)
        val algorithmsQuestionsAndAnswersBytes = evaluation.algorithmsQuestionsAndAnswers.map { list ->
            list.map{
                QuestionAndAnswerSerializer.serialize(it)
            }
        }
        val symptomsBytes = evaluation.symptoms.map { SymptomSerializer.serialize(it) }

        val suggestedTestsBytes = evaluation.suggestedTests.map { LabelledTestSerializer.serialize(it) }
        val requestedTestsBytes = evaluation.requestedTests.map { LabelledTestSerializer.serialize(it) }
        val immediateTreatmentsBytes = evaluation.immediateTreatments.map { ImmediateTreatmentSerializer.serialize(it) }
        val supportiveTherapiesTextsBytes = evaluation.supportiveTherapies.map { TherapyTextSerializer.serialize(it) }
        val additionalTestsBytes = evaluation.additionalTestsText.toByteArray(Charsets.UTF_8)

        val treeAnswersBytes = evaluation.treeAnswers.map { TreeAnswersSerializer.serialize(it) }
        val detailQuestionAnswersBytes = evaluation.detailQuestionAnswers.map { DetailQuestionAnswerSerializer.serialize(it) }

        val buffer = ByteBuffer.allocate(
            4 + visitIdBytes.size +
                    4 + complaintIdBytes.size +
                    4 + symptomsBytes.sumOf { 4 + it.size } +
                    4 + algorithmsQuestionsAndAnswersBytes.sumOf { 4 + it.sumOf { 4 + it.size }} +
                    4 + suggestedTestsBytes.sumOf { 4 + it.size } +
                    4 + requestedTestsBytes.sumOf { 4 + it.size } +
                    4 + immediateTreatmentsBytes.sumOf { 4 + it.size } +
                    4 + supportiveTherapiesTextsBytes.sumOf { 4 + it.size } +
                    4 + additionalTestsBytes.size +
                    4 + treeAnswersBytes.sumOf { 4 + it.size } +
                    4 + detailQuestionAnswersBytes.sumOf { 4 + it.size }
        )

        buffer.putBytes(visitIdBytes)

        buffer.putBytes(complaintIdBytes)

        buffer.putInt(algorithmsQuestionsAndAnswersBytes.size)
        algorithmsQuestionsAndAnswersBytes.forEach { list ->
            buffer.putInt(list.size)
            list.forEach {
                buffer.putBytes(it)
            }
        }

        buffer.putInt(symptomsBytes.size)
        symptomsBytes.forEach {
            buffer.putBytes(it)
        }

        buffer.putInt(suggestedTestsBytes.size)
        suggestedTestsBytes.forEach {
            buffer.putBytes(it)
        }

        buffer.putInt(requestedTestsBytes.size)
        requestedTestsBytes.forEach {
            buffer.putBytes(it)
        }

        buffer.putInt(immediateTreatmentsBytes.size)
        immediateTreatmentsBytes.forEach {
            buffer.putBytes(it)
        }

        buffer.putInt(supportiveTherapiesTextsBytes.size)
        supportiveTherapiesTextsBytes.forEach {
            buffer.putBytes(it)
        }

        buffer.putBytes(additionalTestsBytes)

        buffer.putInt(treeAnswersBytes.size)
        treeAnswersBytes.forEach { buffer.putBytes(it) }

        buffer.putInt(detailQuestionAnswersBytes.size)
        detailQuestionAnswersBytes.forEach { buffer.putBytes(it) }

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Evaluation {
        val buffer = ByteBuffer.wrap(bytes)

        val visitId = buffer.readString()
        val complaintId = buffer.readString()

        val algorithmsQuestionsAndAnswersCount = buffer.int
        val algorithmsQuestionsAndAnswers = List(algorithmsQuestionsAndAnswersCount) {
            val questionsAndAnswersCount = buffer.int
            List(questionsAndAnswersCount) {
                val size = buffer.int
                val itemBytes = ByteArray(size).also { buffer.get(it) }
                QuestionAndAnswerSerializer.deserialize(itemBytes)
            }
        }

        // Symptoms
        val symptomsCount = buffer.int
        val symptoms = List(symptomsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            SymptomSerializer.deserialize(itemBytes)
        }.toSet()

        // Tests
        val suggestedTestsCount = buffer.int
        val suggestedTests = List(suggestedTestsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            LabelledTestSerializer.deserialize(itemBytes)
        }.toSet()

        val requestedTestsCount = buffer.int
        val requestedTests = List(requestedTestsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            LabelledTestSerializer.deserialize(itemBytes)
        }.toSet()

        // Immediate Treatments
        val immediateTreatmentsCount = buffer.int
        val immediateTreatments = List(immediateTreatmentsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            ImmediateTreatmentSerializer.deserialize(itemBytes)
        }.toSet()

        // Supportive Therapies
        val supportiveTherapiesCount = buffer.int
        val supportiveTherapies = List(supportiveTherapiesCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            TherapyTextSerializer.deserialize(itemBytes)
        }.toSet()

        // Additional Tests
        val additionalTests = buffer.readString()

        val treeAnswersCount = buffer.int
        val treeAnswers = List(treeAnswersCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            TreeAnswersSerializer.deserialize(itemBytes)
        }

        val detailQuestionAnswersCount = buffer.int
        val detailQuestionAnswers = List(detailQuestionAnswersCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            DetailQuestionAnswerSerializer.deserialize(itemBytes)
        }

        return Evaluation(
            visitId = visitId,
            complaintId = ComplaintId.complaints[complaintId] ?: error("Complaint id $complaintId not found"),
            algorithmsQuestionsAndAnswers = algorithmsQuestionsAndAnswers,
            symptoms = symptoms,
            suggestedTests = suggestedTests,
            requestedTests = requestedTests,
            immediateTreatments = immediateTreatments,
            supportiveTherapies = supportiveTherapies,
            additionalTestsText = additionalTests,
            treeAnswers = treeAnswers,
            detailQuestionAnswers = detailQuestionAnswers,
        )
    }

    //-----------------------------------
    fun testComplaintSummarySerializer(evaluation: Evaluation) {
        val bytes = serialize(evaluation)
        val recovered = deserialize(bytes)
        val areEqual = evaluation === recovered
        Log.d("Prova", "Original: $evaluation")
        Log.d("Prova", "Recovered: $recovered")
        Log.d("Prova", "areEqual: $areEqual")
    }
}