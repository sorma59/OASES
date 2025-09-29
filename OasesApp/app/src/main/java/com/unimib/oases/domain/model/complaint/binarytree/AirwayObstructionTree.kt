package com.unimib.oases.domain.model.complaint.binarytree

import com.unimib.oases.domain.model.complaint.ImmediateTreatment

data object AirwayObstructionTree: Tree {

    // Questions 
    
    val airWayObstructionQuestion = "Are there signs of airway obstruction (stridor, apnea/gasping, unable to speak or cough)?"

    // Immediate treatments
    
    val airwayObstructionImmediateTreatment = ImmediateTreatment(
        """
            This is a high-risk patient with risk of airway obstruction. 
            Rapidly perform the following actions:
                - check/remove foreign body (abdominal thrusts/back blows/chest thrusts)
                - consider intubation/tracheostomy if needed

            NB: If wheezing/stridor is associated to anaphylaxis (urticaria, shock, GI symptoms) give IM adrenaline 0.01 mg/kg (max 0.5 mg in adults), repeat after 5–15 min as needed. Once the patient has been given adrenaline, also add cetirizine 10 mg (2.5 mg if < 6y or 5 mg if 6-12y) PO + hydrocortisone 4 mg/kg (max 200 mg) IV/IM OR dexamethasone 0.5 mg/kg IV/IM (8 mg for adults) OR prednisone 1 mg/kg PO (max 60 mg)
        """.trimIndent()
    )

    val noAirwayObstructionImmediateTreatment = ImmediateTreatment("The patient does not require immediate treatment for airway obstruction")

    // Leaves

    val airwayObstructionLeaf = LeafNode(
        airwayObstructionImmediateTreatment
    )

    val noAirwayObstructionLeaf = LeafNode(
        noAirwayObstructionImmediateTreatment
    )

    // Internal nodes
    
    override val root = ManualNode(
        airWayObstructionQuestion,
        children = Children(
            left = airwayObstructionLeaf,
            right = noAirwayObstructionLeaf
        )
    )
}