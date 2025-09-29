package com.unimib.oases.domain.model.complaint.binarytree

import com.unimib.oases.domain.model.complaint.ImmediateTreatment

data object SevereRespiratoryDistressTree: Tree {

    // Questions

    val severeRespiratoryDistressQuestion = "Are there signs of severe respiratory distress (SpO2 < 90%, central cyanosis, severe chest indrawing, nasal flaring, grunting, use of accessory muscles, head nodding)?"

    // Immediate treatments

    val severeRespiratoryDistressImmediateTreatment = ImmediateTreatment(
        """
            This is a patient with severe respiratory distress. Rapidly perform the following actions:
                - Give oxygen via nasal cannulas (1-5 L/min) or face mask (up to 10 L/min)
                - target SpO₂ > 90%
        """.trimIndent()
    )

    val noSevereRespiratoryDistressImmediateTreatment = ImmediateTreatment("The patient does not require immediate treatment for severe respiratory distress")

    // Leaves

    val severeRespiratoryDistressLeaf = LeafNode(
        severeRespiratoryDistressImmediateTreatment
    )

    val noSevereRespiratoryDistressLeaf = LeafNode(
        noSevereRespiratoryDistressImmediateTreatment
    )

    // Internal nodes

    override val root = ManualNode(
        value = severeRespiratoryDistressQuestion,
        children = Children(
            left = severeRespiratoryDistressLeaf,
            right = noSevereRespiratoryDistressLeaf
        )
    )

}