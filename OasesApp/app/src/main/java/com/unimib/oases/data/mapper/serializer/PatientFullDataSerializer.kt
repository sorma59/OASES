package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.data.mapper.serializer.PatientDiseaseSerializer.serialize
import com.unimib.oases.data.mapper.serializer.VisitVitalSignSerializer.serialize
import com.unimib.oases.domain.model.PatientFullData
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun ByteBuffer.putNullable(nullableByteArray: ByteArray?){
    nullableByteArray?.let {
        this.put(1) // Presence flag
        this.putInt(nullableByteArray.size)
        this.put(nullableByteArray)
    } ?: this.put(0) // Absence flag
}

fun <T> ByteBuffer.readNullable(deserializer: (ByteArray) -> T): T? {
    val presenceFlag = this.get().toInt()
    return if (presenceFlag == 1) {
        val size = this.int
        val itemBytes = ByteArray(size).also { this.get(it) }
        deserializer(itemBytes)
    } else
        null
}

object PatientFullDataSerializer {

    fun serialize(patientFullData: PatientFullData): ByteArray {

        val patientByteArray = PatientSerializer.serialize(patientFullData.patientDetails)
        val diseaseBytesList = patientFullData.patientDiseases.map { serialize(it) }
        val vitalSignBytesList = patientFullData.vitalSigns.map { serialize(it) }
        val visitByteArray = patientFullData.visit?.let {
            VisitSerializer.serialize(it)
        }
        val triageByteArray = patientFullData.triageEvaluation?.let {
            TriageEvaluationSerializer.serialize(it)
        }
        val malnutritionScreeningBytes = patientFullData.malnutritionScreening?.let {
            MalnutritionScreeningSerializer.serialize(it)
        }
        val complaintSummariesListBytes = patientFullData.complaintsSummaries.map{ComplaintSummarySerializer.serialize(it)}

        val totalSize =
            4 + patientByteArray.size +
            4 + diseaseBytesList.sumOf { 4 + it.size } +
            1 + (visitByteArray?.let {4 + it.size} ?: 0) +
            4 + vitalSignBytesList.sumOf { 4 + it.size } +
            1 + (triageByteArray?.let { 4 + it.size} ?: 0) +
            1 + (malnutritionScreeningBytes?.let { 4 + it.size } ?: 0) +
            4 + complaintSummariesListBytes.sumOf { 4 + it.size }

        val buffer = ByteBuffer.allocate(totalSize).order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(patientByteArray.size)
        buffer.put(patientByteArray)

        buffer.putInt(diseaseBytesList.size)
        diseaseBytesList.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putNullable(visitByteArray)

        buffer.putInt(vitalSignBytesList.size)
        vitalSignBytesList.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putNullable(triageByteArray)

        buffer.putNullable(malnutritionScreeningBytes)

        buffer.putInt(complaintSummariesListBytes.size)
        complaintSummariesListBytes.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): PatientFullData {
        val buffer = ByteBuffer.wrap(bytes)

        // Patient
        val patientSize = buffer.int
        val patientBytes = ByteArray(patientSize).also { buffer.get(it) }
        val patient = PatientSerializer.deserialize(patientBytes)

        // Diseases
        val diseaseCount = buffer.int
        val diseases = List(diseaseCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            PatientDiseaseSerializer.deserialize(itemBytes)
        }

        // Visit
        val visit = buffer.readNullable(VisitSerializer::deserialize)

        // Vitals
        val vitalCount = buffer.int
        val vitals = List(vitalCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            VisitVitalSignSerializer.deserialize(itemBytes)
        }

        // Triage Evaluation
        val triageEvaluation = buffer.readNullable(TriageEvaluationSerializer::deserialize)

        // Malnutrition Screening (nullable)
        val malnutritionScreening = buffer.readNullable(MalnutritionScreeningSerializer::deserialize)

        // Complaint summaries
        val complaintSummariesCount = buffer.int
        val complaintSummaries = List(complaintSummariesCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            ComplaintSummarySerializer.deserialize(itemBytes)
        }

        return PatientFullData(
            patientDetails = patient,
            patientDiseases = diseases,
            vitalSigns = vitals,
            visit = visit,
            triageEvaluation = triageEvaluation,
            malnutritionScreening = malnutritionScreening,
            complaintsSummaries = complaintSummaries
        )
    }
}