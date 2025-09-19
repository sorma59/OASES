package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.binarytree.DiarrheaTree

class Diarrhea(ageInYears: Int): Complaint {

    override val id = ComplaintId.DIARRHEA

    val tree = DiarrheaTree(ageInYears)

    override val details = DiarrheaDetails()

    override val tests = DiarrheaTests()
}