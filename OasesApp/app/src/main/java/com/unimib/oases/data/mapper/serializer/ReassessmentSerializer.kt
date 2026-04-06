package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.Reassessment
import com.unimib.oases.domain.model.complaint.ComplaintId
import java.nio.ByteBuffer

object ReassessmentSerializer {

    fun serialize(reassessment: Reassessment): ByteArray {
        val visitIdBytes = reassessment.visitId.toByteArray(Charsets.UTF_8)
        val complaintIdBytes = reassessment.complaintId.id.toByteArray(Charsets.UTF_8)
        val symptomsBytes = reassessment.symptoms.map { SymptomSerializer.serialize(it) }
        val findingsBytes = reassessment.findings.map { FindingSerializer.serialize(it) }
        val therapiesBytes = reassessment.definitiveTherapies.map { TherapyTextSerializer.serialize(it) }

        val buffer = ByteBuffer.allocate(
            4 + visitIdBytes.size +
                    4 + complaintIdBytes.size +
                    4 + symptomsBytes.sumOf { 4 + it.size } +
                    4 + findingsBytes.sumOf { 4 + it.size } +
                    4 + therapiesBytes.sumOf { 4 + it.size }
        )

        buffer.putBytes(visitIdBytes)

        buffer.putBytes(complaintIdBytes)


        buffer.putInt(symptomsBytes.size)
        symptomsBytes.forEach {
            buffer.putBytes(it)
        }

        buffer.putInt(findingsBytes.size)
        findingsBytes.forEach {
            buffer.putBytes(it)
        }

        buffer.putInt(therapiesBytes.size)
        therapiesBytes.forEach {
            buffer.putBytes(it)
        }

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Reassessment {
        val buffer = ByteBuffer.wrap(bytes)

        val visitId = buffer.readString()
        val complaintId = buffer.readString()

        val symptomsCount = buffer.int
        val symptoms = List(symptomsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            SymptomSerializer.deserialize(itemBytes)
        }.toSet()

        val findingsCount = buffer.int
        val findings = List(findingsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            FindingSerializer.deserialize(itemBytes)
        }.toSet()

        val therapiesCount = buffer.int
        val therapies = List(therapiesCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            TherapyTextSerializer.deserialize(itemBytes)
        }.toSet()

        // Tests

        return Reassessment(
            visitId = visitId,
            complaintId = ComplaintId.complaints[complaintId] ?: error(""),
            symptoms = symptoms,
            findings = findings,
            definitiveTherapies = therapies
        )
    }

}