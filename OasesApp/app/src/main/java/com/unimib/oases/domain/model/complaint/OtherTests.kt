package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Test.AbdominalUltraSound
import com.unimib.oases.domain.model.complaint.Test.AbdominalXRay
import com.unimib.oases.domain.model.complaint.Test.AcidFastBacilliMicroscopy
import com.unimib.oases.domain.model.complaint.Test.BloodSmearForMalariaParasitesTest
import com.unimib.oases.domain.model.complaint.Test.ChestXRay
import com.unimib.oases.domain.model.complaint.Test.CompleteBloodCountTest
import com.unimib.oases.domain.model.complaint.Test.Electrocardiogram
import com.unimib.oases.domain.model.complaint.Test.ElectrolytesTests
import com.unimib.oases.domain.model.complaint.Test.HIVTest
import com.unimib.oases.domain.model.complaint.Test.LimbXRay
import com.unimib.oases.domain.model.complaint.Test.LiverFunctionTests
import com.unimib.oases.domain.model.complaint.Test.MalariaRapidDiagnosticTest
import com.unimib.oases.domain.model.complaint.Test.PelvicXRay
import com.unimib.oases.domain.model.complaint.Test.PregnancyTest
import com.unimib.oases.domain.model.complaint.Test.RapidBloodSugarTest
import com.unimib.oases.domain.model.complaint.Test.RenalFunctionTests
import com.unimib.oases.domain.model.complaint.Test.SicklingTest
import com.unimib.oases.domain.model.complaint.Test.TuberculosisGeneXpert
import com.unimib.oases.domain.model.complaint.Test.Urinalysis
import com.unimib.oases.domain.model.symptom.Symptom

object OtherTests: ComplaintTests {
    override val conditions = listOf(
        OtherBasicTests
    )
}

data object OtherBasicTests: Condition {
    override val label: String = "Consider ordering the following diagnostic tests based on patient presentation"
    override val predicate = { _: Set<Symptom> -> true }
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
            testId = Test.BloodGroupingAndCrossMatching.id,
            label = "Blood grouping and cross-matching if clinical signs of severe anemia (pallor, severe weakness, shock, active bleeding)"
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
            testId = HIVTest.id,
            label = "HIV test if recurrent or severe infections, weight loss, contact with HIV+ partner or HIV+ mother"
        ),
        LabelledTest(
            testId = TuberculosisGeneXpert.id,
            label = "TB GeneXpert on sputum / gastric aspirate / appropriate sample if HIV+, close-contact of TB or TB-related symptoms (fever, night sweats, weight loss, cough ≥ 2 weeks, hemoptysis)"
        ),
        LabelledTest(
            testId = AcidFastBacilliMicroscopy.id,
            label = "Microscopy for acid-fast bacilli on sputum / gastric aspirate / appropriate sample if HIV+, close-contact of TB or TB-related symptoms (fever, night sweats, weight loss, cough, hemoptysis)"
        ),
        LabelledTest(
            testId = PregnancyTest.id,
            label = "Pregnancy test in women of reproductive age with abdominal pain, vaginal bleeding, vomiting."
        ),
        LabelledTest(
            testId = Electrocardiogram.id,
            label = "ECG if chest pain, palpitations, fainting, dyspnea or peripheral edema"
        ),
        LabelledTest(
            testId = RenalFunctionTests.id,
            label = "Renal function tests (creatinine, urea) if suspected renal failure (edema, dyspnea, reduced urine output) or shock"
        ),
        LabelledTest(
            testId = ElectrolytesTests.id,
            label = "Electrolytes (Na, K) if dehydration (vomiting, diarrhea, DKA) or suspected renal failure"
        ),
        LabelledTest(
            testId = Urinalysis.id,
            label = "Urinalysis if UTI symptoms or suspected renal failure (edema, dyspnea, reduced urine output) or hyperglycemia (DKA)"
        ),
        LabelledTest(
            testId = LiverFunctionTests.id,
            label = "Liver function tests (ast, alt, bilirubin) if suspected liver failure (jaundice, ascites, edema, GI bleeding)"
        ),
        LabelledTest(
            testId = SicklingTest.id,
            label = "Sickling test if suspected sickle cell disease (severe anemia, recurrent severe malaria, recurrent severe infections, splenomegaly)"
        ),
        LabelledTest(
            testId = ChestXRay.id,
            label = "Chest X-ray in patients with respiratory symptoms (cough, dyspnea, respiratory distress), chest pain or chest trauma"
        ),
        LabelledTest(
            testId = AbdominalXRay.id,
            label = "Abdominal X-ray if suspected intestinal obstruction or perforation"
        ),
        LabelledTest(
            testId = PelvicXRay.id,
            label = "Pelvic X-ray if high-energy trauma or suspected pelvic fracture"
        ),
        LabelledTest(
            testId = LimbXRay.id,
            label = "Limb X-ray if suspected fracture/dislocation"
        ),
        LabelledTest(
            testId = AbdominalUltraSound.id,
            label = "Abdominal US if suspected abdominal disease, trauma or if pregnancy-related complaint"
        ),
    )
}