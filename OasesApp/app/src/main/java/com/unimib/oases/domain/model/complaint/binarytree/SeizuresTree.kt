package com.unimib.oases.domain.model.complaint.binarytree

import com.unimib.oases.domain.model.complaint.ImmediateTreatment
import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.Sex

class SeizuresTree(
    sex: Sex,
    patientCategory: PatientCategory
): Tree {

    // Questions

    val activeTonicClonicSeizuresQuestion = "Are there active tonic-clonic seizures?"

    val twentyPlusWeeksPregnancyQuestion = "Is the patient pregnant at > 20 weeks?"

    // Immediate treatments
    val activeTonicClonicSeizuresAndTwentyPlusWeeksPregnancyImmediateTreatment = ImmediateTreatment(
        """
            Suspect eclampsia in patients with seizures after 20 weeks of gestation, associated with hypertension (BP > 140/90) and proteinuria.
            Eclampsia is a medical emergency!
              - Call for OBGYN: deliver the baby by the safest and fastest means available (within 6-12 hours).
              - MgSO4: 4 g IV loading dose over 20 min (draw 8 mL of MgSO4 50% 5 g/10 ml and add 12 mL of water for injection/NS, then 5 g IM in each buttock (10 mL of MgSO4 50% solution, undiluted) with 1 mL of 2% lidocaine in the same syringe. After this continue with 5 g IM (10 mL of MgSO4 50% solution) every 4 hours in alternate buttocks for 24 hours from the time of loading dose or after the last convulsion, whichever comes first.
              - Hydralazine: 5–10 mg IV bolus for BP >160/110 mmHg every 30 minutes until diastolic is BP is down to < 100 mmHg
              - Nifedipine retard: 20 mg 12 hourly until delivery after controlling BP with hydralazine
        """.trimIndent()
    )

    val activeTonicClonicSeizuresWithoutTwentyPlusWeeksPregnancyImmediateTreatment = ImmediateTreatment(
        """
            If convulsions are present, do not restrain the patient or put anything in the mouth, position the patient in recovery position and give O2 if SpO2 < 90%. Promptly check RBS and temperature.
            If convulsions are prolonged (> 5 min) or repeated (≥ 2 discrete seizure without complete recovery of consciousness), give diazepam:
              - adults: diazepam 5-10 mg slow IV injection
              - children: diazepam 0.2 mg/kg slow IV injection (maximum 10 mg)
            A second injection of diazepam can be repeated after 10 minutes if convulsions have not stopped
            NB: Always have a working bag and mask of appropriate size available in case the patient stops breathing. Monitor SpO2, HR and BP

            If IV access is not available, use rectal diazepam (max 20 mg):
              - 0.5 mg/kg for ages 2–5 years
              - 0.3 mg/kg for ages 6–11 years
              - 0.2 mg/kg for ages ≥12 years
            NB: If diarrhea is present, avoid using rectal diazepam

            If the patient still has convulsions after 2 doses of diazepam, give EITHER:
              - phenobarbital 20 mg/kg (max 1000 mg) IV loading dose. Infusion in 15-20 min (rate no faster than 50 mg/min to prevent respiratory depression and hypotension), diluted 1:10 in water for injection/NS (AVOID dilution in D5%). Can be repeated 2x at a dose of 10 mg/kg IV. Maintenance dose: 2.5 mg/kg bid
              - phenytoin 20 mg/kg IV by slow infusion over 30-60 min (Flush IV catheter with NS before infusion). Dilution ONLY in NS. AVOID water for infusion and D5%. Final concentration should be ≤ 10 mg/mL

            If high fever is present, also give paracetamol (avoid oral root until fully conscious) and undress the patient to reduce the fever. If hypoglycemia, also give IV glucose (see below).  
        """.trimIndent()
    )

    val noActiveTonicClonicSeizuresImmediateTreatment = ImmediateTreatment("No immediate treatment needed")

    // Nodes

     // Leaves

    val activeTonicClonicSeizuresAndTwentyPlusWeeksPregnancyLeaf = LeafNode(
        activeTonicClonicSeizuresAndTwentyPlusWeeksPregnancyImmediateTreatment
    )

    val activeTonicClonicSeizuresWithoutTwentyPlusWeeksPregnancyLeaf = LeafNode(
        activeTonicClonicSeizuresWithoutTwentyPlusWeeksPregnancyImmediateTreatment
    )

    val noActiveTonicClonicSeizuresLeaf = LeafNode(
        noActiveTonicClonicSeizuresImmediateTreatment
    )

     // Internal nodes

    val activeTonicClonicSeizuresNode = AutoNode(
        value = twentyPlusWeeksPregnancyQuestion,
        children = Children(
            left = activeTonicClonicSeizuresAndTwentyPlusWeeksPregnancyLeaf,
            right = activeTonicClonicSeizuresWithoutTwentyPlusWeeksPregnancyLeaf
        ),
        predicate = {
            if (sex != Sex.FEMALE
                || patientCategory == PatientCategory.PEDIATRIC
            )
                false
            else
                null
        }
    )

    override val root = ManualNode(
        value = activeTonicClonicSeizuresQuestion,
        children = Children(
            left = activeTonicClonicSeizuresNode,
            right = noActiveTonicClonicSeizuresLeaf
        )
    )
}