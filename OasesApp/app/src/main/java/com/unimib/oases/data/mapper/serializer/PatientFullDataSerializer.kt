package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.data.bluetooth.transfer.PatientFullData
import com.unimib.oases.data.mapper.serializer.PatientDiseaseSerializer.serialize
import com.unimib.oases.data.mapper.serializer.VisitVitalSignSerializer.serialize
import java.nio.ByteBuffer
import java.nio.ByteOrder

object PatientFullDataSerializer {

    fun serialize(patientFullData: PatientFullData): ByteArray {

        val patientByteArray = PatientSerializer.serialize(patientFullData.patientDetails)
        val diseaseBytesList = patientFullData.patientDiseases.map { serialize(it) }
        val vitalSignBytesList = patientFullData.vitalSigns.map { serialize(it) }
        val visitByteArray = VisitSerializer.serialize(patientFullData.visit)
        val triageByteArray = TriageEvaluationSerializer.serialize(patientFullData.triageEvaluation)

        val buffer = ByteBuffer.allocate(
            4 + patientByteArray.size +
            4 + diseaseBytesList.sumOf { 4 + it.size } +
            4 + visitByteArray.size +
            4 + vitalSignBytesList.sumOf { 4 + it.size } +
            4 + triageByteArray.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(patientByteArray.size)
        buffer.put(patientByteArray)

        buffer.putInt(diseaseBytesList.size)
        diseaseBytesList.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putInt(visitByteArray.size)
        buffer.put(visitByteArray)

        buffer.putInt(vitalSignBytesList.size)
        vitalSignBytesList.forEach {
            buffer.putInt(it.size)
            buffer.put(it)
        }

        buffer.putInt(triageByteArray.size)
        buffer.put(triageByteArray)

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
        val visitSize = buffer.int
        val visitBytes = ByteArray(visitSize).also { buffer.get(it) }
        val visit = VisitSerializer.deserialize(visitBytes)

        // Vitals
        val vitalCount = buffer.int
        val vitals = List(vitalCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            VisitVitalSignSerializer.deserialize(itemBytes)
        }

        // Triage Evaluation
        val triageSize = buffer.int
        val triageBytes = ByteArray(triageSize).also { buffer.get(it) }
        val triageEvaluation = TriageEvaluationSerializer.deserialize(triageBytes)

        return PatientFullData(
            patientDetails = patient,
            patientDiseases = diseases,
            vitalSigns = vitals,
            visit = visit,
            triageEvaluation = triageEvaluation
        )
    }
}