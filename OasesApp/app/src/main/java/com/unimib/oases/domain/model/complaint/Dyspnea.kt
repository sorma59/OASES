package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.binarytree.AirwayObstructionTree
import com.unimib.oases.domain.model.complaint.binarytree.SevereRespiratoryDistressTree
import com.unimib.oases.domain.model.complaint.binarytree.WheezingTree

data object Dyspnea: Complaint {
    override val complaintId = ComplaintId.DYSPNEA
    override val immediateTreatmentTrees = listOf(
        AirwayObstructionTree,
        SevereRespiratoryDistressTree,
        WheezingTree
    )
    override val details = DyspneaDetails
    override val tests = DyspneaTests
    override val supportiveTherapies = DyspneaSupportiveTherapies
}