package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Fever
import com.unimib.oases.domain.model.symptom.Pain
import com.unimib.oases.domain.model.symptom.Symptom

object SeizuresOrComaSupportiveTherapies: ComplaintSupportiveTherapies {
    override val therapies = listOf(
        SeizuresOrComaWithHypoglycemia,
        SeizuresOrComaWithFeverOrPain,
        SeizuresOrComaWithHypotensionOrShock,
        SeizuresOrComaWithHypertensiveEmergency
    )
}

data object SeizuresOrComaWithHypoglycemia: SupportiveTherapy(
    SupportiveTherapyText("""
        If hypoglycemia is present (RBS < 2.5 mmol/L in a well-nourished or < 3 mmol/L in a severely malnourished child), give rapidly IV glucose:
          - Adults: D50% 1 ml/kg IV, check RBG after 15 min, repeat if low
          - Children: D10% 5 ml/kg IV, check RBG after 15 min, repeat if low

        NB: Sublingual sugar may be used as an immediate ‘first aid’ measure in managing hypoglycemia if IV access is impossible or delayed. Place one level teaspoonful of sugar moistened with water under the tongue every 10–20 min.

        To avoid recurrence, feed the patient as soon as he is able (normal consciousness). If the patient is unable to feed without danger of aspiration, give maintenance IV fluids with 5-10% glucose
    """.trimIndent()),
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.Hypoglycemia)
    }
)

data object SeizuresOrComaWithFeverOrPain: SupportiveTherapy(
    SupportiveTherapyText("""
       If fever or pain are present, give paracetamol
         - IV paracetamol dosage: 1 gr in 100 of normal saline (children: 15 mg/kg)
         - oral paracetamol dosage: 1 gr (children: 15 mg/kg or 7.5mg/kg if >10 kg) maximum 3 times/day
       NB: If diarrhea is present, avoid using rectal paracetamol
       With high fever, always consider presence of a bacterial infection (pneumonia) or malaria; with fever lasting > 14 days, always consider presence of TB infection
   """.trimIndent()),
    { symptoms: Set<Symptom> ->
        symptoms.any { it is Fever || it is Pain }
    }
)

data object SeizuresOrComaWithHypotensionOrShock: SupportiveTherapy(
    SupportiveTherapyText("""
        If hypotension / signs of shock are present without signs of volume overload and without severe malnutrition, set 2 large bore IV lines and infuse IV fluids (Ringer's lactate or Normal Saline 0.9%) 20 ml/kg boluses (up to 1 L in adults) in 30 min according to response (repeat up to 2 times).
        Monitor closely for worsening signs of fluid overload (increasing respiratory rate or heart rate, worsening difficulty breathing, inability to lie flat and increasing crackles in the chest and peripheral edema): stop IV fluids if any of these signs develop.
    """.trimIndent()),
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.Shock)
        || symptoms.contains(Symptom.Hypotension)
    }
)

data object SeizuresOrComaWithHypertensiveEmergency: SupportiveTherapy(
    SupportiveTherapyText("If hypertensive emergency (BP >180/110 mmHg with symptoms and acute life threatening complications, eg. seizures, visual disturbance, severe headache): give hydralazine 10 mg IV over 20 minutes. Check blood pressure regularly, repeat dose after 20-30 minutes if necessary."),
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.HypertensiveEmergency)
    }
)