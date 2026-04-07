package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.DispositionEntity
import com.unimib.oases.domain.model.Disposition
import com.unimib.oases.domain.model.DispositionType

fun DispositionEntity.toDomain() = Disposition(
    visitId = visitId,
    dispositionType = when (ward) {
        null -> DispositionType.Discharge
        else -> DispositionType.Hospitalization(ward)
    },
    dispositionTypeLabel = dispositionTypeLabel,
    homeTreatments = homeTreatments.toSet(),
    prescribedTherapiesText = prescribedTherapiesText,
    finalDiagnosisText = finalDiagnosisText,
)

fun Disposition.toEntity() = DispositionEntity(
    visitId = visitId,
    ward = when (val dt = dispositionType) {
        is DispositionType.Discharge -> null
        is DispositionType.Hospitalization -> dt.ward
    },
    dispositionTypeLabel = dispositionTypeLabel,
    homeTreatments = homeTreatments.toList(),
    prescribedTherapiesText = prescribedTherapiesText,
    finalDiagnosisText = finalDiagnosisText,
)