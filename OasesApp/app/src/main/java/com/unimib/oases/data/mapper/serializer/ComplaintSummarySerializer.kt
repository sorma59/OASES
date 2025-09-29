package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.ComplaintSummary
import java.nio.ByteBuffer

object ComplaintSummarySerializer {

    fun serialize(complaintSummary: ComplaintSummary): ByteArray {
        val complaintIdBytes = complaintSummary.complaintId.toByteArray(Charsets.UTF_8)
        val symptomsBytes = complaintSummary.symptoms.map { SymptomSerializer.serialize(it) }
        val testsBytes = complaintSummary.tests.map { LabelledTestSerializer.serialize(it) }
        val immediateTreatmentsBytes = complaintSummary.immediateTreatments.map { ImmediateTreatmentSerializer.serialize(it) }
        val supportiveTherapiesTextsBytes = complaintSummary.supportiveTherapies.map { SupportiveTherapyTextSerializer.serialize(it) }
        val additionalTestsBytes = complaintSummary.additionalTests.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + complaintIdBytes.size +
                    4 + symptomsBytes.sumOf { 4 + it.size } +
                    4 + testsBytes.sumOf { 4 + it.size } +
                    4 + immediateTreatmentsBytes.sumOf { 4 + it.size } +
                    4 + supportiveTherapiesTextsBytes.sumOf { 4 + it.size } +
                    4 + additionalTestsBytes.size
        )

        buffer.putInt(complaintIdBytes.size)
        buffer.put(complaintIdBytes)

        buffer.putInt(symptomsBytes.size)
        symptomsBytes.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putInt(testsBytes.size)
        testsBytes.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putInt(immediateTreatmentsBytes.size)
        immediateTreatmentsBytes.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putInt(supportiveTherapiesTextsBytes.size)
        supportiveTherapiesTextsBytes.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putInt(additionalTestsBytes.size)
        buffer.put(additionalTestsBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): ComplaintSummary {
        val buffer = ByteBuffer.wrap(bytes)

        val complaintId = buffer.readString()

        // Symptoms
        val symptomsCount = buffer.int
        val symptoms = List(symptomsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            SymptomSerializer.deserialize(itemBytes)
        }.toSet()

        // Tests
        val testsCount = buffer.int
        val tests = List(testsCount) {
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
            SupportiveTherapyTextSerializer.deserialize(itemBytes)
        }.toSet()

        // Additional Tests
        val additionalTests = buffer.readString()

        return ComplaintSummary(
            complaintId = complaintId,
            symptoms = symptoms,
            tests = tests,
            immediateTreatments = immediateTreatments,
            supportiveTherapies = supportiveTherapies,
            additionalTests = additionalTests
        )
    }

    //-----------------------------------
    fun test(complaintSummary: ComplaintSummary) {
        val bytes = serialize(complaintSummary)
        val recovered = deserialize(bytes)
        Log.d("Prova", "Original: $complaintSummary")
        Log.d("Prova", "Recovered: $recovered")
    }
}