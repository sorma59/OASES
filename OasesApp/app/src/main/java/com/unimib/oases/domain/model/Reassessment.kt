package com.unimib.oases.domain.model

import com.unimib.oases.domain.model.complaint.ComplaintId
import com.unimib.oases.domain.model.complaint.Finding
import com.unimib.oases.domain.model.complaint.TherapyText
import com.unimib.oases.domain.model.symptom.Symptom

data class Reassessment(
    val visitId: String,
    val complaintId: ComplaintId,
    val symptoms: Set<Symptom>,
    val findings: Set<Finding>,
    val definitiveTherapies: Set<TherapyText>,  // suggested & shown, no selection
)