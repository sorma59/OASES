package com.unimib.oases.domain.model.complaints

import com.unimib.oases.util.datastructure.binarytree.DiarrheaTree

class Diarrhea(ageInYears: Int): Complaint {

    override val id = ComplaintId.DIARRHEA

    val tree = DiarrheaTree(ageInYears)

}