package com.unimib.oases.ui.screen.medical_visit.reassessment

import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.complaint.Complaint
import com.unimib.oases.domain.model.complaint.DefinitiveTherapy
import com.unimib.oases.domain.model.complaint.Finding
import com.unimib.oases.domain.model.symptom.Symptom

data class ReassessmentState(
    val patientId: String,
    val visitId: String,
    val complaintId: String,

    val patient: Patient? = null,
    val complaint: Complaint? = null,

    val symptoms: Set<Symptom> = emptySet(),
    val findings: Set<Finding> = emptySet(),

    val definitiveTherapies: Set<DefinitiveTherapy>? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val possibleFindings: Set<Finding>
        get() = complaint?.findings?.findings.orEmpty()

    val shouldShowSubmitButton: Boolean
        get() = definitiveTherapies != null
}