package com.unimib.oases.data.mapper

import com.unimib.oases.data.local.model.FindingSnapshot
import com.unimib.oases.data.local.model.ReassessmentEntity
import com.unimib.oases.domain.model.Reassessment
import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.findingsById
import com.unimib.oases.domain.model.symptom.symptomsById

fun Reassessment.toEntity(): ReassessmentEntity {
    return ReassessmentEntity(
        visitId = visitId,
        complaintId = complaintId.id,
        symptomIds = symptoms.map { it.id },
        findings = findings.map { FindingSnapshot(it.id, it.description) },
        definitiveTherapies = definitiveTherapies.toList(),
    )
}

fun ReassessmentEntity.toDomain(): Reassessment {
    return Reassessment(
        visitId = visitId,
        complaintId = ComplaintId.complaints[complaintId] ?: error("Complaint id $complaintId not found"),
        symptoms = symptomIds.map { symptomsById[it] ?: error("Symptom id $it not found") }.toSet(),
        findings = findings.map { findingsById[it.id] ?: error("Findings id $it not found") }.toSet(),
        definitiveTherapies = definitiveTherapies.toSet()
    )
}