package com.unimib.oases.data.mapper.serializer

import com.unimib.oases.domain.model.Disposition
import com.unimib.oases.domain.model.DispositionType
import com.unimib.oases.domain.model.Ward
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.ui.screen.medical_visit.disposition.HomeTreatment
import java.nio.ByteBuffer

object DispositionSerializer {

    fun serialize(disposition: Disposition): ByteArray {
        val visitIdBytes = disposition.visitId.toByteArray(Charsets.UTF_8)
        val isHospitalization = disposition.dispositionType is DispositionType.Hospitalization
        val wardBytes = if (isHospitalization) {
            disposition.dispositionType.ward.name.toByteArray(Charsets.UTF_8)
        } else null
        val dispositionTypeLabelBytes = disposition.dispositionTypeLabel.toByteArray(Charsets.UTF_8)
        val homeTreatmentBytes = disposition.homeTreatments
            .map { it.complaintId.id.toByteArray(Charsets.UTF_8) }
        val prescribedTherapiesBytes = disposition.prescribedTherapiesText.toByteArray(Charsets.UTF_8)
        val finalDiagnosisBytes = disposition.finalDiagnosisText.toByteArray(Charsets.UTF_8)

        val buffer = ByteBuffer.allocate(
            4 + visitIdBytes.size +
                    1 + (if (wardBytes != null) 4 + wardBytes.size else 0) +
                    4 + dispositionTypeLabelBytes.size +
                    4 + homeTreatmentBytes.sumOf { 4 + it.size } +
                    4 + prescribedTherapiesBytes.size +
                    4 + finalDiagnosisBytes.size
        )

        buffer.putBytes(visitIdBytes)

        buffer.put(if (isHospitalization) 1 else 0)
        if (wardBytes != null) buffer.putBytes(wardBytes)

        buffer.putBytes(dispositionTypeLabelBytes)

        buffer.putInt(homeTreatmentBytes.size)
        homeTreatmentBytes.forEach { buffer.putBytes(it) }

        buffer.putBytes(prescribedTherapiesBytes)
        buffer.putBytes(finalDiagnosisBytes)

        return buffer.array()
    }

    fun deserialize(bytes: ByteArray): Disposition {
        val buffer = ByteBuffer.wrap(bytes)

        val visitId = buffer.readString()

        val isHospitalization = buffer.get() == 1.toByte()
        val dispositionType = if (isHospitalization) {
            DispositionType.Hospitalization(Ward.valueOf(buffer.readString()))
        } else {
            DispositionType.Discharge
        }

        val dispositionTypeLabel = buffer.readString()

        val homeTreatmentsCount = buffer.int
        val homeTreatments = List(homeTreatmentsCount) {
            val size = buffer.int
            val itemBytes = ByteArray(size).also { buffer.get(it) }
            val id = String(itemBytes, Charsets.UTF_8)
            when (id) {
                ComplaintId.DIARRHEA.id -> HomeTreatment.Diarrhea
                ComplaintId.DYSPNEA.id -> HomeTreatment.Dyspnea
                ComplaintId.SEIZURES_OR_COMA.id -> HomeTreatment.SeizuresOrComa
                ComplaintId.OTHER.id -> HomeTreatment.OtherComplaints
                else -> error("Unknown HomeTreatment id: $id")
            }
        }.toSet()

        val prescribedTherapiesText = buffer.readString()
        val finalDiagnosisText = buffer.readString()

        return Disposition(
            visitId = visitId,
            dispositionType = dispositionType,
            dispositionTypeLabel = dispositionTypeLabel,
            homeTreatments = homeTreatments,
            prescribedTherapiesText = prescribedTherapiesText,
            finalDiagnosisText = finalDiagnosisText,
        )
    }
}