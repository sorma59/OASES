package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.binarytree.SeizuresTree
import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.Sex

class SeizuresOrComa(
    sex: Sex,
    patientCategory: PatientCategory
): Complaint {
    override val complaintId = ComplaintId.SEIZURES_OR_COMA
    override val immediateTreatmentTrees = listOf(
        SeizuresTree(sex, patientCategory)
    )
    override val details = SeizuresOrComaDetails
    override val tests: ComplaintTests
        get() = TODO("Not yet implemented")
    override val supportiveTherapies: ComplaintSupportiveTherapies
        get() = TODO("Not yet implemented")
}