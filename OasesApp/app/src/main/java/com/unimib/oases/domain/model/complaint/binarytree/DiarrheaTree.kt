package com.unimib.oases.domain.model.complaint.binarytree

import com.unimib.oases.domain.model.complaint.ImmediateTreatment

class DiarrheaTree(val ageInYears: Int): Tree {

    // Questions

    val shockOrSevereDehydrationQuestion = "Are there signs of shock/severe dehydration (lethargic/unconscious, weak rapid pulse, cold extremities, capillary refill > 3 sec, hypotension, unable to drink)?"

    val moderateDehydrationQuestion = "Are there signs of moderate dehydration (restless/irritable, sunken eyes and fontanels, skin pinch returns slowly, reduced urine output, drinks eagerly)?"

    val malnutritionQuestion = "Is the patient malnourished?"

    // Immediate treatments
    val shockOrSevereDehydrationAndMalnutritionImmediateTreatment = ImmediateTreatment(
        """
            This is a high-risk patient with shock/severe dehydration and malnutrition.
            Rapidly perform the following actions:
                - set 2 large bore IV lines\
                - infuse 15 ml/kg IV in 1 hour using Ringer’s lactate (RL) with 5% glucose (dextrose) → remove 50 ml from RL 500 ml and add 50 ml of dextrose 50%. Use 0.45% NaCl plus 5% glucose (dextrose) if RL not available
                - if there are signs of improvement (pulse rate falls, pulse volume increases or respiratory rate falls) and no evidence of pulmonary edema, repeat IV infusion at 15 ml/kg over 1 h
                - then switch to oral or nasogastric rehydration with ReSoMal at 10 ml/kg/hr up to 10 h
                - initiate re-feeding with starter F-75
                - hospitalize high-risk malnourished children
                If the patient deteriorates during IV rehydration (breathing rate increases by 5 bpm and pulse rate increases by 15 bpm, liver enlarges, fine crackles throughout lung fields, jugular venous pressure increases, galloping heart rhythm develops), stop the infusion, because IV fluid can induce pulmonary edema.
        """.trimIndent()
    )

    val shockOrSevereDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderImmediateTreatment =
        ImmediateTreatment(
            """
            This is a high-risk patient with shock/severe dehydration. 
            Rapidly perform the following actions:
                - set 2 large bore IV lines
                - infuse IV fluids (Ringer's lactate or Normal Saline 0.9%) 20 ml/kg boluses according to response (repeat up to 3 times)
                - assess response to fluid resuscitation by monitoring BP, HR, RR, capillary refill, mental status and urinary output
                - after initial stabilization with fluid boluses, continue IV infusion with Ringer’s lactate (or Normal Saline 0.9%) 50 ml/kg in the first 4 hours (up to a maximum of 3 L)
                - as soon as patient can drink, also give ORS 5 ml/kg/hour. In addition to ORS, other fluids such as soup, fruit juice and safe clean water may be given
                - continued nutrition is important and food should be continued during treatment for dehydration
                - consider hospitalizing the patient to continue treatment

            NB: If IV fluids are not available within 30 minutes, start rehydration by nasogastric tube (or by mouth) with ORS at 20 ml/kg/hr for 6 h (total: 120 ml/kg divided in 6 hours)This is a high-risk patient with shock/severe dehydration. Rapidly perform the following actions:
                - set 2 large bore IV lines
                - infuse IV fluids (Ringer's lactate or Normal Saline 0.9%) 20 ml/kg boluses according to response (repeat up to 3 times)
                - assess response to fluid resuscitation by monitoring BP, HR, RR, capillary refill, mental status and urinary output
                - after initial stabilization with fluid boluses, continue IV infusion with Ringer’s lactate (or Normal Saline 0.9%) 50 ml/kg in the first 4 hours (up to a maximum of 3 L)
                - as soon as patient can drink, also give ORS 5 ml/kg/hour. In addition to ORS, other fluids such as soup, fruit juice and safe clean water may be given
                - continued nutrition is important and food should be continued during treatment for dehydration
                - consider hospitalizing the patient to continue treatment

            NB: If IV fluids are not available within 30 minutes, start rehydration by nasogastric tube (or by mouth) with ORS at 20 ml/kg/hr for 6 h (total: 120 ml/kg divided in 6 hours)
        """.trimIndent()
        )

    val shockOrSevereDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldImmediateTreatment =
        ImmediateTreatment(
            """
            This is a high-risk patient with shock/severe dehydration. Rapidly perform the following actions:
                - set 2 large bore IV lines
                - infuse IV fluids (Ringer's lactate or Normal Saline 0.9%) 20 ml/kg boluses according to response (repeat up to 3 times)
                - assess response to fluid resuscitation by monitoring BP, HR, RR, capillary refill, mental status and urinary output
                - after initial stabilization with fluid boluses, continue IV infusion with Ringer’s lactate (or Normal Saline 0.9%) according to the following scheme:
                   If < 12 months: 30 ml/Kg in 1 hour, then 70 ml/kg in 5 hours
                   If > 12 months: 30 ml/Kg in 30 min, then 70 ml/kg in 2.5 hour
                - as soon as patient can drink, also give ORS 5 ml/kg/hour. In addition to ORS, other fluids such as soup, fruit juice and safe clean water may be given
                - continued nutrition is important and food should be continued during treatment for dehydration
                - consider hospitalizing the patient to continue treatment. Otherwise, observe the child for at least 6 h after rehydration to be sure that the mother can maintain hydration by giving the child ORS solution by mouth.

            NB: If IV fluids are not available within 30 minutes, start rehydration by nasogastric tube (or by mouth) with ORS at 20 ml/kg/hr for 6 h (total: 120 ml/kg divided in 6 hours)
        """.trimIndent()
        )

    val moderateDehydrationAndMalnutritionImmediateTreatment = ImmediateTreatment(
        """
            This is a high-risk patient with moderate dehydration and malnutrition.
            Rapidly perform the following actions:
                - give oral ReSoMal 5 ml/kg every 30 min for the first 2 h.
                - then continue oral ReSoMal 5–10 ml/kg per/hr for the next 4–10 h on alternate hours
                - initiate re-feeding with starter F-75
                - hospitalize high-risk malnourished children
        """.trimIndent()
    )

    val moderateDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderImmediateTreatment = ImmediateTreatment(
        """
            This is a patient with moderate dehydration. Perform the following actions:
                - give ORS 50 ml/kg in the first 4 hours. Initially, adults can take up to 750 ml ORS/hour (3 L in 4 hours)
                - observe for at least 4hr, then if the patient improves and tolerates ORS, discharge with ORS
        """.trimIndent()
    )

    val moderateDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldImmediateTreatment =
        ImmediateTreatment(
            """
            This is a patient with moderate dehydration. Perform the following actions:
                - give ORS 75 ml/kg in the first 4 hours. Give frequent small sips from a cup. If the child vomits, wait 10 minutes, then continue more slowly
                - for infants < 6 months who are not breastfed, also give 100-200 ml of clean water during the first 4 hours
                - observe for at least 4 hr, then if the patient improves and tolerates ORS, discharge with ORS
        """.trimIndent()
        )

    val noShockNorSevereDehydrationAndNoMalnutritionTherapy = ImmediateTreatment("No immediate treatment needed")

    // Nodes

      // Leaves
    val shockOrSevereDehydrationAndMalnutritionLeaf = LeafNode(
          shockOrSevereDehydrationAndMalnutritionImmediateTreatment
      )

    val shockOrSevereDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderLeaf = LeafNode(
        shockOrSevereDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderImmediateTreatment
    )

    val shockOrSevereDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldLeaf = LeafNode(
        shockOrSevereDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldImmediateTreatment
    )

    val moderateDehydrationAndMalnutritionLeaf = LeafNode(
        moderateDehydrationAndMalnutritionImmediateTreatment
    )

    val moderateDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderLeaf = LeafNode(
        moderateDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderImmediateTreatment
    )

    val moderateDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldLeaf = LeafNode(
        moderateDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldImmediateTreatment
    )

    val noShockNorSevereDehydrationAndNoMalnutritionLeaf = LeafNode(
        noShockNorSevereDehydrationAndNoMalnutritionTherapy
    )

      // Internal nodes

    val moderateDehydrationWithoutMalnutritionNode = AutoNode(
        children = Children(
            left = moderateDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderLeaf,
            right = moderateDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldLeaf
        ),
        predicate = {
            ageInYears >= 5
        }
    )

    val shockOrSevereDehydrationWithoutMalnutritionNode = AutoNode(
        children = Children(
            left = shockOrSevereDehydrationWithoutMalnutritionAndFiveYearsOldOrOlderLeaf,
            right = shockOrSevereDehydrationWithoutMalnutritionAndYoungerThanFiveYearsOldLeaf
        ),
        predicate = {
            ageInYears >= 5
        }
    )

    val moderateDehydrationNode = ManualNode(
        value = malnutritionQuestion,
        children = Children(
            left = moderateDehydrationAndMalnutritionLeaf,
            right = moderateDehydrationWithoutMalnutritionNode
        )
    )

    val shockOrSevereDehydrationNode = ManualNode(
        value = malnutritionQuestion,
        children = Children(
            left = shockOrSevereDehydrationAndMalnutritionLeaf,
            right = shockOrSevereDehydrationWithoutMalnutritionNode
        )
    )

    val noShockNorSevereDehydrationNode = ManualNode(
        value = moderateDehydrationQuestion,
        children = Children(
            left = moderateDehydrationNode,
            right = noShockNorSevereDehydrationAndNoMalnutritionLeaf
        )
    )

    override val root = ManualNode(
        value = shockOrSevereDehydrationQuestion,
        children = Children(
            left = shockOrSevereDehydrationNode,
            right = noShockNorSevereDehydrationNode
        )
    )
}