package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.PatientDisease
import java.nio.ByteBuffer
import java.nio.ByteOrder

object PatientDiseaseSerializer {

    fun serialize(patientDisease: PatientDisease): ByteArray {
        val patientIdBytes = patientDisease.patientId.toByteArray(Charsets.UTF_8)
        val diseaseNameBytes = patientDisease.diseaseName.toByteArray(Charsets.UTF_8)
        val diagnosisDateBytes = patientDisease.diagnosisDate.toByteArray(Charsets.UTF_8)
        val additionalInfoBytes = patientDisease.additionalInfo.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
                4 + patientIdBytes.size +
                4 + diseaseNameBytes.size +
                4 + diagnosisDateBytes.size +
                4 + additionalInfoBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putInt(patientIdBytes.size)
        buffer.put(patientIdBytes)

        buffer.putInt(diseaseNameBytes.size)
        buffer.put(diseaseNameBytes)

        buffer.putInt(diagnosisDateBytes.size)
        buffer.put(diagnosisDateBytes)

        buffer.putInt(additionalInfoBytes.size)
        buffer.put(additionalInfoBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): PatientDisease {
        val buffer = ByteBuffer.wrap(bytes)

        val patientId = buffer.readString()
        val diseaseName = buffer.readString()
        val diagnosisDate = buffer.readString()
        val additionalInfo = buffer.readString()

        return PatientDisease(
            patientId = patientId,
            diseaseName = diseaseName,
            diagnosisDate = diagnosisDate,
            additionalInfo = additionalInfo
        )
    }

    // ----------------Testing--------------------
    fun test() {
        val original = PatientDisease(
            patientId = "id",
            diseaseName = "name",
            diagnosisDate = "date",
            additionalInfo = "info"
        )
        Log.d("PatientDiseaseSerializer", "Original: $original")
        val bytes = serialize(original)
        val recovered = deserialize(bytes)
        Log.d("PatientDiseaseSerializer", "Recovered: $recovered")
    }

}