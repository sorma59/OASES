package com.unimib.oases.domain.model.complaint.binarytree

import com.unimib.oases.domain.model.complaint.ImmediateTreatment

data object WheezingTree: Tree {

    // Questions

    val wheezingQuestion = "Is there wheezing?"

    // Immediate treatments

    val wheezingImmediateTreatment = ImmediateTreatment(
        """
            This is a patient with suspected bronchospasm.
            Rapidly perform the following actions:
              - administer salbutamol → either
                • Inhaler+spacer: 2 puffs (up to 6 puffs if ≤5 y and up to 10 puffs if >5 y), may be repeated every 20 minutes in the first hour
                • Nebulized (at 6–9 L/min): 2.5 mg (child) / 5 mg (adult) in 2–4 ml saline; may be repeated every 20 min in the first hour
              - If salbutamol unavailable: use adrenaline 1 mg/ml → SC adrenaline 0.01 ml/kg or nebulized adrenaline 2–4 ml in 2–4 ml saline.
              - If salbutamol not effective: IV magnesium sulfate (MgSO4 50%) 50mg/kg (maximum 2g) by slow infusion over 20 minutes

            NB: If wheezing/stridor is associated to anaphylaxis (urticaria, shock, GI symptoms) give IM adrenaline 0.01 mg/kg (max 0.5 mg in adults), repeat after 5–15 min as needed. Once the patient has been given adrenaline, also add cetirizine 10 mg (2.5 mg if < 6y or 5 mg if 6-12y) PO + hydrocortisone 4 mg/kg (max 200 mg) IV/IM OR dexamethasone 0.5 mg/kg IV/IM (8 mg for adults) OR prednisone 1 mg/kg PO (max 60 mg)
        """.trimIndent()
    )

    val noWheezingImmediateTreatment = ImmediateTreatment("The patient does not require immediate treatment related to wheezing")

    // Leaves

    val wheezingLeaf = LeafNode(
        wheezingImmediateTreatment
    )

    val noWheezingLeaf = LeafNode(
        noWheezingImmediateTreatment
    )

    // Internal nodes

    override val root = ManualNode(
        value = wheezingQuestion,
        children = Children(
            left = wheezingLeaf,
            right = noWheezingLeaf
        )
    )
}