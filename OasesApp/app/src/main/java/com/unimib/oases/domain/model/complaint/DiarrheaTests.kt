package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Test.AbdominalXRay
import com.unimib.oases.domain.model.complaint.Test.BloodSmearForMalariaParasitesTest
import com.unimib.oases.domain.model.complaint.Test.CompleteBloodCountTest
import com.unimib.oases.domain.model.complaint.Test.HIVTest
import com.unimib.oases.domain.model.complaint.Test.MalariaRapidDiagnosticTest
import com.unimib.oases.domain.model.complaint.Test.RapidBloodSugarTest
import com.unimib.oases.domain.model.complaint.Test.StoolMicroscopy
import com.unimib.oases.domain.model.symptom.Symptom

object DiarrheaTests: ComplaintTests {
    override val conditions = listOf(
        DiarrheaBasicTests,
        DiarrheaRiskOfBacterialOrParasiticInfectionsTests,
        DiarrheaRiskOfToxicMegacolonOrIntestinalOcclusionOrPerforation,
        DiarrheaRiskOfHIVTests
    )
}

data object DiarrheaBasicTests: Condition {
    override val label: String = "Consider ordering the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> -> true }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = RapidBloodSugarTest.id,
            label = "Rapid blood sugar (RBS) in patients with severe illness, inability to breastfeed, malnutrition, altered mental status, convulsions"
        ),
        LabelledTest(
            testId = CompleteBloodCountTest.id,
            label = "Complete blood count (CBC) in patients with severe illness, suspected sepsis, signs of anemia, suspected HUS (easy bruising)"
        ),
        LabelledTest(
            testId = MalariaRapidDiagnosticTest.id,
            label = "Malaria rapid diagnostic test (MRDT) in patients with severe illness, fever, pallor"
        ),
        LabelledTest(
            testId = BloodSmearForMalariaParasitesTest.id,
            label = "Blood smear for malaria parasites (B/S) in patients with severe illness, fever, pallor"
        )
    )
}

data object DiarrheaRiskOfBacterialOrParasiticInfectionsTests: Condition {
    override val label = "This is a patient at risk of bacterial/parasitic infections. Order also the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.HivPositive)
        || symptoms.contains(Symptom.DiarrheaInTheLastFifteenToThirtyDays)
        || symptoms.contains(Symptom.DiarrheaInTheLastThirtyPlusDays)
        || symptoms.contains(Symptom.DiarrheaBloodyStools)
        || symptoms.contains(Symptom.DiarrheaOilyOrGreasyOrFoulSmellingStools)
    }
    override val suggestedTests = listOf(
        LabelledTest(StoolMicroscopy.id)
    )
}

data object DiarrheaRiskOfToxicMegacolonOrIntestinalOcclusionOrPerforation: Condition {
    override val label = "This is a patient at risk of toxic megacolon or intestinal occlusion/perforation. Order also an abdominal X-ray"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.AbdominalDistensionAndTendernessWithAlteredBowelSounds)
    }
    override val suggestedTests = listOf(
        LabelledTest(AbdominalXRay.id)
    )
}

data object DiarrheaRiskOfHIVTests: Condition {
    override val label = "This is a patient at risk of HIVTest due to chronic diarrhea. Order also an HIVTest test"
    override val predicate = { symptoms: Set<Symptom> ->
        !symptoms.contains(Symptom.HivPositive)
        && symptoms.contains(Symptom.DiarrheaInTheLastThirtyPlusDays)
    }
    override val suggestedTests = listOf(
        LabelledTest(HIVTest.id)
    )
}