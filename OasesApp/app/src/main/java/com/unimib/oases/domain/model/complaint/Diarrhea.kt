package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.binarytree.DiarrheaTree

class Diarrhea(ageInYears: Int): Complaint {

    override val complaintId = ComplaintId.DIARRHEA

    override val immediateTreatmentTrees = listOf(DiarrheaTree(ageInYears))

    override val details = DiarrheaDetails

    override val tests = DiarrheaTests

    override val supportiveTherapies = DiarrheaSupportiveTherapies
}