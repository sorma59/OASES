package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Test.AcidFastBacilliMicroscopy
import com.unimib.oases.domain.model.complaint.Test.BloodSmearForMalariaParasitesTest
import com.unimib.oases.domain.model.complaint.Test.ChestXRay
import com.unimib.oases.domain.model.complaint.Test.CompleteBloodCountTest
import com.unimib.oases.domain.model.complaint.Test.Echocardiography
import com.unimib.oases.domain.model.complaint.Test.Electrocardiogram
import com.unimib.oases.domain.model.complaint.Test.ElectrolytesTests
import com.unimib.oases.domain.model.complaint.Test.HIVTest
import com.unimib.oases.domain.model.complaint.Test.LiverFunctionTests
import com.unimib.oases.domain.model.complaint.Test.MalariaRapidDiagnosticTest
import com.unimib.oases.domain.model.complaint.Test.RapidBloodSugarTest
import com.unimib.oases.domain.model.complaint.Test.RenalFunctionTests
import com.unimib.oases.domain.model.complaint.Test.TuberculosisGeneXpert
import com.unimib.oases.domain.model.complaint.Test.Urinalysis
import com.unimib.oases.domain.model.complaint.Test.VenousUltrasoundOfLowerLimbs
import com.unimib.oases.domain.model.symptom.Pregnancy
import com.unimib.oases.domain.model.symptom.Symptom

object DyspneaTests: ComplaintTests {
    override val conditions = listOf(
        DyspneaBasicTests,
        DyspneaRiskOfTuberculosisTests,
        DyspneaRiskOfHIVTests,
        DyspneaRiskOfAcuteHeartDiseaseTests,
        DyspneaRiskOfFluidOverloadDueToHeartOrRenalOrLiverFailureTests
    )
}

data object DyspneaBasicTests: Condition {
    override val label: String = "Consider ordering the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> -> true }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = RapidBloodSugarTest.id,
            label = "Rapid blood sugar (RBS) in patients with severe illness, altered mental status, convulsions, inability to eat/breastfeed, malnutrition"
        ),
        LabelledTest(
            testId = CompleteBloodCountTest.id,
            label = "Complete blood count (CBC) in patients with severe illness, respiratory distress, fever, signs of anemia, HIVTest"
        ),
        LabelledTest(
            testId = MalariaRapidDiagnosticTest.id,
            label = "Malaria rapid diagnostic test (MRDT) in patients with severe illness, fever, pallor"
        ),
        LabelledTest(
            testId = BloodSmearForMalariaParasitesTest.id,
            label = "Blood smear for malaria parasites (B/S) in patients with severe illness, fever, pallor"
        ),
        LabelledTest(
            testId = ChestXRay.id,
            label = "Chest x-ray in patients with severe illness, respiratory distress, fever, chronic cough or other symptoms suggestive of TB, HIVTest, traumatic chest injury"
        )
    )
}

data object DyspneaRiskOfTuberculosisTests: Condition {
    override val label = "This is a patient at risk of TB. Order also the a TB test"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.DyspneaInTheLastFifteenToThirtyDays)
        || symptoms.contains(Symptom.DyspneaInTheLastThirtyPlusDays)
        || symptoms.contains(Symptom.DyspneaCoughWithBlood)
        || symptoms.contains(Symptom.HivPositive)
        || symptoms.contains(Symptom.CloseContactWithKnownTuberculosisPatient)
    }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = TuberculosisGeneXpert.id,
            label = "TB GeneXpert on sputum / gastric aspirate / appropriate sample"
        ),
        LabelledTest(
            testId = AcidFastBacilliMicroscopy.id,
            label = "Microscopy for acid-fast bacilli on sputum / gastric aspirate / appropriate sample"
        )
    )
}

data object DyspneaRiskOfHIVTests: Condition {
    override val label = "This is a patient at risk of HIV. Order also an HIV test"
    override val predicate = { symptoms: Set<Symptom> ->
        !symptoms.contains(Symptom.HivPositive)
        && (symptoms.contains(Symptom.DyspneaInTheLastThirtyPlusDays)
            || symptoms.contains(Symptom.DyspneaInTheLastFifteenToThirtyDays)
            || symptoms.contains(Symptom.DyspneaCoughWithBlood)
        )
    }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = HIVTest.id,
        )
    )
}

data object DyspneaRiskOfAcuteHeartDiseaseTests: Condition {
    override val label = "This is a patient at risk of acute heart disease (ischemic heart disease / cardiac arrhythmia / pulmonary embolism). Consider ordering the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.OrthopneaOrParoxysmalNocturnalDyspnea)
        || symptoms.contains(Symptom.DyspneaCoughWithBlood)
        || symptoms.contains(Symptom.ProlongedImmobilizationOrBedridden)
        || symptoms.contains(Symptom.SwellingOrPainOrWarmthOrRednessOfOneLeg)
        || symptoms.contains(Symptom.CardiacMurmurAtChestAuscultation)
        || symptoms.contains(Symptom.ChestPain)
        || symptoms.contains(Symptom.IrregularPulseOrIrregularHeartSounds)
        || symptoms.contains(Symptom.HistoryOfHeartDisease)
        || symptoms.any { symptom -> symptom is Pregnancy}
        || symptoms.contains(Symptom.PeripheralEdemaOrJugularVenousDistension)
    }
    override val suggestedTests = listOf(
        LabelledTest(
            testId = Electrocardiogram.id,
            label = "ECG"
        ),
        LabelledTest(
            testId = Echocardiography.id
        ),
        LabelledTest(
            testId = VenousUltrasoundOfLowerLimbs.id,
            label = "Venous ultrasound of lower limbs with suspect of pulmonary embolism"
        )
    )
}

data object DyspneaRiskOfFluidOverloadDueToHeartOrRenalOrLiverFailureTests: Condition {
    override val label = "This is a patient at risk of fluid overload due to heart failure or liver/renal failure. Consider ordering the following diagnostic tests"
    override val predicate = { symptoms: Set<Symptom> ->
        symptoms.contains(Symptom.HistoryOfHeartDisease)
        || symptoms.contains(Symptom.HistoryOfRenalOrLiverDisease)
        || symptoms.contains(Symptom.PeripheralEdemaOrJugularVenousDistension)
        || symptoms.contains(Symptom.OrthopneaOrParoxysmalNocturnalDyspnea)
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