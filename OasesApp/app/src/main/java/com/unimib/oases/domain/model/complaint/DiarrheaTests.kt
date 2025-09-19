package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Test.BloodSmearForMalariaParasites
import com.unimib.oases.domain.model.complaint.Test.CompleteBloodCount
import com.unimib.oases.domain.model.complaint.Test.MalariaRapidDiagnosticTest
import com.unimib.oases.domain.model.complaint.Test.RapidBloodSugar
import com.unimib.oases.domain.model.symptom.Symptom

class DiarrheaTests: ComplaintTests {
    override val conditions = listOf(
        DiarrheaBasicTests,
        DiarrheaRiskOfBacterialOrParasiticInfectionsTests,
        DiarrheaRiskOfToxicMegacolonOrIntestinalOcclusionOrPerforation,
        DiarrheaRiskOfHIV
    )
}

data object DiarrheaBasicTests: Condition {
    override val label: String = "Consider ordering the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> -> true }
    override val suggestedTests: List<Test> = listOf(
        RapidBloodSugar(
            label = "Rapid blood sugar (RBS) in patients with severe illness, inability to breastfeed, malnutrition, altered mental status, convulsions"
        ),
        CompleteBloodCount(
            label = "Complete blood count (CBC) in patients with severe illness, suspected sepsis, signs of anemia, suspected HUS (easy bruising)"
        ),
        MalariaRapidDiagnosticTest(
            label = "Malaria rapid diagnostic test (MRDT) in patients with severe illness, fever, pallor"
        ),
        BloodSmearForMalariaParasites(
            label = "Blood smear for malaria parasites (B/S) in patients with severe illness, fever, pallor"
        )
    )
}

data object DiarrheaRiskOfBacterialOrParasiticInfectionsTests: Condition {
    override val label = "This is a patient at risk of bacterial/parasitic infections. Order also the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.HivPositive) ||
                symptoms.contains(Symptom.DiarrheaInTheLastOneToSevenDays) ||
                symptoms.contains(Symptom.DiarrheaInTheLastThirtyPlusDays) ||
                symptoms.contains(Symptom.DiarrheaBloodyStools) ||
                symptoms.contains(Symptom.DiarrheaOilyOrGreasyOrFoulSmellingStools)
    }
    override val suggestedTests = listOf(
        Test.StoolMicroscopy()
    )
}

data object DiarrheaRiskOfToxicMegacolonOrIntestinalOcclusionOrPerforation: Condition {
    override val label = "This is a patient at risk of toxic megacolon or intestinal occlusion/perforation. Order also an abdominal X-ray"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.AbdominalDistensionAndTendernessWithAlteredBowelSounds)
    }
    override val suggestedTests = listOf(
        Test.AbdominalXRay()
    )
}

data object DiarrheaRiskOfHIV: Condition {
    override val label = "This is a patient at risk of HIV due to chronic diarrhea. Order also an HIV test"
    override val predicate = { symptoms: Set<Symptom> ->
        !symptoms.contains(Symptom.HivPositive) &&
                symptoms.contains(Symptom.DiarrheaInTheLastThirtyPlusDays)
    }
    override val suggestedTests = listOf(
        Test.HIV()
    )
}