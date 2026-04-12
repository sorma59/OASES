package com.unimib.oases.ui.screen.medical_visit.reassessment.summary

import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.Finding
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.domain.model.symptom.Symptom

data class ReassessmentSummaryState(
    val patientId: String,
    val visitId: String,
    val complaintId: String,

    val complaint: Complaint? = null,

    val symptoms: Set<Symptom> = emptySet(),
    val findings: Set<Finding> = emptySet(),

    val definitiveTherapies: Set<TherapyText> = emptySet(),

    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val possibleFindings: Set<Finding>
        get() = complaint?.findings?.findings.orEmpty()

}