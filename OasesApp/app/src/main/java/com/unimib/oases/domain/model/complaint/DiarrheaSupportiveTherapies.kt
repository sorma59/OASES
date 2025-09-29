package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Fever
import com.unimib.oases.domain.model.symptom.Pain
import com.unimib.oases.domain.model.symptom.Symptom

object DiarrheaSupportiveTherapies: ComplaintSupportiveTherapies {
    override val therapies = listOf(
        DiarrheaWithFeverOrPain,
        DiarrheaWithConvulsions,
        DiarrheaWithHypoglycemia
    )
}

data object DiarrheaWithFeverOrPain: SupportiveTherapy(
    SupportiveTherapyText("""
        If fever or pain are present, give paracetamol.
          - IV paracetamol dosage: 1 gr in 100 of normal saline (children: 15 mg/kg)
          - oral paracetamol dosage: 1 gr (children: 15 mg/kg or 7.5mg/kg if < 10 kg)) maximum 3 times/day
        NB: If diarrhea is present, avoid using rectal paracetamol
        With high fever, always consider presence of a bacterial infection or malaria
    """.trimIndent()),
    { symptoms: Set<Symptom> ->
        symptoms.any { it is Fever || it is Pain }
    }
)

data object DiarrheaWithConvulsions: SupportiveTherapy(
    SupportiveTherapyText("If convulsions are present, do not restrain the patient or put anything in the mouth, position the patient in recovery position and give O2 if SpO2 < 90%. Promptly check RBS and temperature."),
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.Convulsions)
    }
)

data object DiarrheaWithHypoglycemia: SupportiveTherapy(
    SupportiveTherapyText("""
        If hypoglycemia is present (RBS < 2.5 mmol/L in a well-nourished or < 3 mmol/L in a severely malnourished child), give rapidly IV glucose:
          - Adults: D50% 1 ml/kg IV, check RBG after 15 min, repeat if low
          - Children: D10% 5 ml/kg IV, check RBG after 15 min, repeat if low

        NB: Sublingual sugar may be used as an immediate ‘first aid’ measure in managing hypoglycemia if IV access is impossible or delayed. Place one level teaspoonful of sugar moistened with water under the tongue every 10–20 min.

        To avoid recurrence, feed the patient (or give ORS) as soon as he is able (normal consciousness). If the patient is unable to feed without danger of aspiration, give maintenance IV fluids with 5-10% glucose.
    """.trimIndent()),
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.Hypoglycemia)
    }
)