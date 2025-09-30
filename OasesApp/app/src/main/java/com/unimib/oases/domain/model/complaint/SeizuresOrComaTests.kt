package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Test.AcidFastBacilliMicroscopy
import com.unimib.oases.domain.model.complaint.Test.BloodSmearForMalariaParasitesTest
import com.unimib.oases.domain.model.complaint.Test.CompleteBloodCountTest
import com.unimib.oases.domain.model.complaint.Test.ElectrolytesTests
import com.unimib.oases.domain.model.complaint.Test.HIVTest
import com.unimib.oases.domain.model.complaint.Test.LiverFunctionTests
import com.unimib.oases.domain.model.complaint.Test.LumbarPunctureAndCSFExamination
import com.unimib.oases.domain.model.complaint.Test.MalariaRapidDiagnosticTest
import com.unimib.oases.domain.model.complaint.Test.RapidBloodSugarTest
import com.unimib.oases.domain.model.complaint.Test.RenalFunctionTests
import com.unimib.oases.domain.model.complaint.Test.SkullXRay
import com.unimib.oases.domain.model.complaint.Test.TuberculosisGeneXpert
import com.unimib.oases.domain.model.complaint.Test.Urinalysis
import com.unimib.oases.domain.model.symptom.Fever
import com.unimib.oases.domain.model.symptom.SeizuresOrComaConsciousness
import com.unimib.oases.domain.model.symptom.Symptom

object SeizuresOrComaTests: ComplaintTests {
    override val conditions = listOf(
        SeizuresOrComaBasicTests,
        SeizuresOrComaRiskOfCNSInfectionTests,
        SeizuresOrComaRiskOfTuberculosisTests,
        SeizuresOrComaRiskOfHIVTests,
        SeizuresOrComaPatientWithHeadInjuryTests,
        SeizuresOrComaRiskOfRenalOrLiverFailureOrElectrolytesDisorderTests
    )
}

data object SeizuresOrComaBasicTests: Condition {
    override val label: String = "Consider ordering the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> -> true }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = RapidBloodSugarTest.id,
            label = "Rapid blood sugar (RBS) in all patients"
        ),
        LabelledTest(
            testId = CompleteBloodCountTest.id,
            label = "Complete blood count (CBC) in patients with severe illness, fever, signs of anemia, HIV"
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

data object SeizuresOrComaRiskOfCNSInfectionTests: Condition {
    override val label = "This is a patient with possible CNS infection. Also consider performing lumbar puncture with CSF examination if no signs of intracranial hypertension"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.any { it is Fever }
        && (
            symptoms.contains(Symptom.CloseContactWithKnownTuberculosisPatient)
            || symptoms.contains(Symptom.HivPositive)
            || symptoms.contains(Symptom.PetechialRashOrPurpura)
            || symptoms.contains(Symptom.HeadacheOrCervicalPain)
            || symptoms.contains(Symptom.StiffNeck)
            || symptoms.contains(Symptom.BulgingFontanelle)
            || symptoms.any {
                it is SeizuresOrComaConsciousness
                && it != Symptom.AlertLevelOfConsciousness
            }
        )
    }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = LumbarPunctureAndCSFExamination.id,
            label = "Lumbar puncture and CSF examination"
        )
    )
}

data object SeizuresOrComaRiskOfTuberculosisTests: Condition {
    override val label = "This is a patient at risk of TB. Order also the a TB test"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.SeizuresOrComaInTheLastFifteenToThirtyDays)
        || symptoms.contains(Symptom.SeizuresOrComaInTheLastThirtyPlusDays)
        || symptoms.contains(Symptom.HivPositive)
        || symptoms.contains(Symptom.CloseContactWithKnownTuberculosisPatient)
    }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = TuberculosisGeneXpert.id,
            label = "TB GeneXpert on CSF / sputum / gastric aspirate"
        ),
        LabelledTest(
            testId = AcidFastBacilliMicroscopy.id,
            label = "Microscopy for acid-fast bacilli on CSF / sputum / gastric aspirate"
        )
    )
}

data object SeizuresOrComaRiskOfHIVTests: Condition {
    override val label = "This is a patient at risk of HIV. Order also an HIV test"
    override val predicate = { symptoms: Set<Symptom> ->
        !symptoms.contains(Symptom.HivPositive)
        && (symptoms.contains(Symptom.SeizuresOrComaInTheLastFifteenToThirtyDays)
            || symptoms.contains(Symptom.SeizuresOrComaInTheLastThirtyPlusDays)
        )
    }
    override val suggestedTests = listOf(
        LabelledTest(
            HIVTest.id
        )
    )
}

data object SeizuresOrComaPatientWithHeadInjuryTests: Condition {
    override val label = "This is a patient with head injury. Order also a skull x-ray"
    override val predicate = { symptoms : Set<Symptom> ->
        symptoms.contains(Symptom.HeadInjury)
    }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = SkullXRay.id
        )
    )
}

data object SeizuresOrComaRiskOfRenalOrLiverFailureOrElectrolytesDisorderTests: Condition {
    override val label = "This is a patient at risk of liver/renal failure and electrolytes disorder. Consider ordering the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.CurrentPregnancy)
        || symptoms.contains(Symptom.HistoryOfRenalOrLiverDisease)
        || symptoms.contains(Symptom.PeripheralEdemaOrJugularVenousDistension)
        || symptoms.contains(Symptom.Jaundice)
    }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = RenalFunctionTests.id,
            label = "Renal function tests (creatinine, urea)"
        ),
        LabelledTest(
            testId = ElectrolytesTests.id,
            label = "Electrolytes (Na, K)"
        ),
        LabelledTest(
            testId = Urinalysis.id
        ),
        LabelledTest(
            testId = LiverFunctionTests.id,
            label = "Liver function tests (ast, alt, bilirubin)"
        )
    )
}