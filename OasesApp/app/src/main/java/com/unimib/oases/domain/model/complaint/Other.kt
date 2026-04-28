package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.binarytree.Tree

object Other: Complaint {
    override val complaintId = ComplaintId.OTHER
    override val immediateTreatmentTrees = emptyList<Tree>()
    override val details = OtherDetails
    override val tests = OtherTests
    override val supportiveTherapies = OtherSupportiveTherapies
    override val findings = OtherFindings
    override val definitiveTherapies = OtherDefinitiveTherapies
}