package com.unimib.oases.domain.model

data class PatientWithVisitInfo(
    val patient: Patient,
    val visit: Visit
) {
    fun getIds(): PatientAndVisitIds {
        return PatientAndVisitIds(
            patient.id,
            visit.id
        )
    }
}