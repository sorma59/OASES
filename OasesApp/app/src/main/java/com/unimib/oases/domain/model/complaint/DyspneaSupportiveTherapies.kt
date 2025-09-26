package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Fever
import com.unimib.oases.domain.model.symptom.Pain
import com.unimib.oases.domain.model.symptom.Symptom

object DyspneaSupportiveTherapies: ComplaintSupportiveTherapies {
    override val therapies = listOf(
        DyspneaWithFeverOrPain,
        DyspneaWithConvulsions,
        DyspneaWithHypoglycemia,
        DyspneaWithHypotensionOrShock,
        DyspneaWithHypertensiveEmergency
    )
}

data object DyspneaWithFeverOrPain: SupportiveTherapy(
    """
        If fever or pain are present, give paracetamol
          - IV paracetamol dosage: 1 gr in 100 of normal saline (children: 15 mg/kg)
          - oral paracetamol dosage: 1 gr (children: 15 mg/kg or 7.5mg/kg if >10 kg) maximum 3 times/day
        NB: If diarrhea is present, avoid using rectal paracetamol
        With high fever, always consider presence of a bacterial infection (pneumonia) or malaria; with fever lasting > 14 days, always consider presence of TB infection
    """.trimIndent(),
    { symptoms: Set<Symptom> ->
        symptoms.any { it is Fever || it is Pain }
    }
)

data object DyspneaWithConvulsions: SupportiveTherapy(
    """
        If convulsions are present, do not restrain the patient or put anything in the mouth, position the patient in recovery position and give O2 if SpO2 < 90%. Promptly check RBS and temperature.

        If convulsions are prolonged (> 5 min) or repeated (≥ 2 discrete seizure without complete recovery of consciousness), give diazepam:
          - adults: diazepam 5-10 mg slow IV injection
          - children: diazepam 0.2 mg/kg slow IV injection (maximum 10 mg)
        A second injection of diazepam can be repeated after 10 minutes if convulsions have not stopped
        NB: Always have a working bag and mask of appropriate size available in case the patient stops breathing. Monitor SpO2, HR and BP

        If IV access is not available, it is possible to use rectal diazepam (max 20 mg):
          - 0.5 mg/kg for ages 2–5 years
          - 0.3 mg/kg for ages 6–11 years
          - 0.2 mg/kg for ages ≥12 years
        NB: If diarrhea is present, avoid using rectal diazepam

        If the patient still has convulsions after 2 doses of diazepam, give EITHER:
          - phenobarbital 20 mg/kg (max 1000 mg) IV loading dose. Infusion in 15-20 min (rate no faster than 50 mg/min to prevent respiratory depression and hypotension), diluted 1:10 in water for injection/NS (AVOID dilution in D5%). Can be repeated 2x at a dose of 10 mg/kg IV. Maintenance dose: 2.5 mg/kg bid
          - phenytoin 20 mg/kg IV by slow infusion over 30-60 min (Flush IV catheter with NS before infusion). Dilution ONLY in NS. AVOID water for infusion and D5%. Final concentration should be ≤ 10 mg/mL

        If high fever is present, also give paracetamol (avoid oral root until fully conscious) and undress the patient to reduce the fever. If hypoglycemia, also give IV glucose.
    """.trimIndent(),
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.Convulsions)
    }
)

data object DyspneaWithHypoglycemia: SupportiveTherapy(
    """
        If hypoglycemia is present (RBS < 2.5 mmol/L in a well-nourished or < 3 mmol/L in a severely malnourished child), give rapidly IV glucose:
          - Adults: D50% 1 ml/kg IV, check RBG after 15 min, repeat if low
          - Children: D10% 5 ml/kg IV, check RBG after 15 min, repeat if low
        
        NB: Sublingual sugar may be used as an immediate ‘first aid’ measure in managing hypoglycemia if IV access is impossible or delayed. Place one level teaspoonful of sugar moistened with water under the tongue every 10–20 min.
        
        To avoid recurrence, feed the patient as soon as he is able (normal consciousness). If the patient is unable to feed without danger of aspiration, give maintenance IV fluids with 5-10% glucose
    """.trimIndent(),
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.Hypoglycemia)
    }
)

data object DyspneaWithHypotensionOrShock: SupportiveTherapy(
    "If hypotension / signs of shock are present without signs of volume overload and without severe malnutrition, set 2 large bore IV lines and infuse IV fluids (Ringer's lactate or Normal Saline 0.9%) 20 ml/kg boluses (up to 1 L in adults) in 30 min according to response (repeat up to 2 times).Monitor closely for worsening signs of fluid overload (increasing respiratory rate or heart rate, worsening difficulty breathing, inability to lie flat and increasing crackles in the chest and peripheral edema): stop IV fluids if any of these signs develop.",
    { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.Shock)
    }
)

data object DyspneaWithHypertensiveEmergency: SupportiveTherapy(
    "If hypertensive emergency (BP >180/110 mmHg with symptoms and acute life threatening complications, eg. pulmonary edema): give hydralazine 10 mg IV over 20 minutes. Check blood pressure regularly, repeat dose after 20-30 minutes if necessary",
    { symptoms: Set<Symptom> ->
        true //TODO("Convert vital signs into symptoms")
    }
)