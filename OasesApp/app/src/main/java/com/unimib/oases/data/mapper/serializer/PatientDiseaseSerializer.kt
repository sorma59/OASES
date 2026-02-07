package com.unimib.oases.data.mapper.serializer

import android.util.Log
import com.unimib.oases.domain.model.PatientDisease
import java.nio.ByteBuffer
import java.nio.ByteOrder

object PatientDiseaseSerializer {

    fun serialize(patientDisease: PatientDisease): ByteArray {
        val patientIdBytes = patientDisease.patientId.toByteArray(Charsets.UTF_8)
        val diseaseNameBytes = patientDisease.diseaseName.toByteArray(Charsets.UTF_8)
        val isDiagnosedBytes = patientDisease.isDiagnosed.toString().toByteArray(Charsets.UTF_8)
        val diagnosisDateBytes = patientDisease.diagnosisDate.toByteArray(Charsets.UTF_8)
        val additionalInfoBytes = patientDisease.additionalInfo.toByteArray(Charsets.UTF_8)
        val freeTextBytes = patientDisease.freeTextValue.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
                4 + patientIdBytes.size +
                4 + diseaseNameBytes.size +
                4 + isDiagnosedBytes.size +
                4 + diagnosisDateBytes.size +
                4 + additionalInfoBytes.size +
                4 + freeTextBytes.size
        ).order(ByteOrder.BIG_ENDIAN)

        buffer.putBytes(patientIdBytes)

        buffer.putBytes(diseaseNameBytes)

        buffer.putBytes(isDiagnosedBytes)

        buffer.putBytes(diagnosisDateBytes)

        buffer.putBytes(additionalInfoBytes)

        buffer.putBytes(freeTextBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): PatientDisease {
        val buffer = ByteBuffer.wrap(bytes)

        val patientId = buffer.readString()
        val diseaseName = buffer.readString()
        val isDiagnosed = buffer.readString().toBooleanStrict()
        val diagnosisDate = buffer.readString()
        val additionalInfo = buffer.readString()
        val freeText = buffer.readString()

        return PatientDisease(
            patientId = patientId,
            diseaseName = diseaseName,
            isDiagnosed = isDiagnosed,
            diagnosisDate = diagnosisDate,
            additionalInfo = additionalInfo,
            freeTextValue = freeText
        )
    }

    // ----------------Testing--------------------
    fun test() {
        val original = PatientDisease(
            patientId = "id",
            diseaseName = "name",
            isDiagnosed = true,
            diagnosisDate = "date",
            additionalInfo = "info",
            freeTextValue = "aaaaaaaaaaaaaaaaaaaaaa"
        )
        Log.d("PatientDiseaseSerializer", "Original: $original")
        val bytes = serialize(original)
        val recovered = deserialize(bytes)
        Log.d("PatientDiseaseSerializer", "Recovered: $recovered")
    }

}