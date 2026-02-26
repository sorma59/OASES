package com.unimib.oases.domain.model.complaint

sealed interface Finding {

    val description: String

    data object HighWhiteBloodCells: Finding {
        override val description = "White blood cells >10.000/µl in adults and >20.000/µl in children"
    }

    data object LowHemoglobin: Finding {
        override val description = "Hb < 10 g/dl"
    }

    data object LowPlateletCount: Finding {
        override val description = "Platelets < 150.000/µl"
    }

    data object MalariaRapidDiagnosticTestPositive: Finding {
        override val description = "Malaria rapid diagnostic test (MRDT) positive"
    }

    data object BloodSmearPositiveForMalaria: Finding {
        override val description = "Blood smear (B/S) positive for malaria"
    }

    data object EntamoebaHistolyticaPositive: Finding {
        override val description = "Stool microscopy positive for Entamoeba histolytica"
    }

    data object GiardiaPositive: Finding {
        override val description = "Stool microscopy positive for Giardia"
    }

    data object CryptosporidiumPositive: Finding {
        override val description = "Stool microscopy positive for Cryptosporidium"
    }

    data object IntestinalWormsPositive: Finding {
        override val description = "Stool microscopy positive for intestinal worms"
    }

    data object ToxicMegacolonOrIntestinalObstruction: Finding {
        override val description = "Abdominal x-ray showing toxic megacolon or intestinal obstruction"
    }

    data object HIVTestPositive: Finding {
        override val description = "HIV test positive"
    }

    data object TuberculosisGeneXpertOrMicroscopyPositive: Finding {
        override val description = "TB GeneXpert or microscopy positive"
    }

    data object ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia: Finding {
        override val description = "CXR (or physical examination in children) suggestive of pneumonia"
    }

    data object ChestXRaySuggestiveOfPleuralEffusion: Finding {
        override val description = "CXR suggestive of pleural effusion"
    }

    data object ChestXRaySuggestiveOfPneumothorax: Finding {
        override val description = "CXR suggestive of PNX"
    }

    data object ChestXRaySuggestiveOfTuberculosis: Finding {
        override val description = "CXR suggestive of TB"
    }

    data object ChestXRaySuggestiveOfPulmonaryEdema: Finding {
        override val description = "CXR suggestive of pulmonary edema"
    }

    data object EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponade: Finding {
        override val description = "Echocardiography or CXR suggestive of pericardial effusion/tamponade"
    }

    data object EchoCardiographySuggestiveOfMyocardialInfarction: Finding {
        override val description = "ECG suggestive of myocardial infarction"
    }

    data object EchoCardiographySuggestiveOfAtrialFibrillation: Finding {
        override val description = "ECG suggestive of atrial fibrillation"
    }

    data object VenousUltraSoundSuggestiveOfDeepVeinThrombosis: Finding {
        override val description = "Venous US suggestive of deep vein thrombosis"
    }

    data object CerebrospinalFluidSuggestiveOfBacterialInfection: Finding {
        override val description = "CSF suggestive of bacterial infection (cloudy, WBC > 1000 PMN/mm3, glucose < 1.5 mmol/l, proteins > 0.4 g/l or Pandy test positive, Gram +/- organisms)"
    }

    data object CerebrospinalFluidSuggestiveOfTuberculosisMeningitis: Finding {
        override val description = "CSF suggestive of TB meningitis (TB GeneXpert positive, moderate WBC, mainly lymphocytes, low glucose, high proteins)"
    }

    data object CerebrospinalFluidSuggestiveOfCryptococcalMeningitis: Finding {
        override val description = "CSF suggestive of cryptococcal meningitis (CragAg positive or India Ink stain positive)"
    }

    data object CerebrospinalFluidSuggestiveOfNeurosyphilis: Finding {
        override val description = "CSF suggestive of neurosyphilis (CSF VDRL/RPR/TPHA positive)"
    }

    data object SkullXRaySuggestiveOfSkullFracture: Finding {
        override val description = "Skull x-ray suggestive of skull fracture"
    }

    data object UrineAnalysisWithProteinuria: Finding {
        override val description = "Urine analysis with proteinuria"
    }

    data object CreatinineOrUreaElevated: Finding {
        override val description = "RFTs (creatinine, urea) elevated"
    }

    data object ASTOrALTOrBilirubinElevated: Finding {
        override val description = "LFTs (AST/ALT/bilirubin) elevated"
    }

    data object AlteredElectrolytes: Finding {
        override val description = "Altered electrolytes (Na, K, Ca, Mg)"
    }

    data object PregnancyTestPositive: Finding {
        override val description = "Pregnancy test positive"
    }
}
