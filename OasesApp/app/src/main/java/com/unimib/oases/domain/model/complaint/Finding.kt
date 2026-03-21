package com.unimib.oases.domain.model.complaint

import com.unimib.oases.util.StringFormatHelper.SnakeCaseString
import com.unimib.oases.util.StringFormatHelper.snakeCase

sealed interface Finding {
    val findingId: FindingId
    val description: String

    val id: String
        get() = findingId.value.string

    data object HighWhiteBloodCells: Finding {
        override val findingId = FindingId.HighWhiteBloodCellsId
        override val description = "White blood cells >10.000/µl in adults and >20.000/µl in children"
    }

    data object LowHemoglobin: Finding {
        override val findingId = FindingId.LowHemoglobinId
        override val description = "Hb < 10 g/dl"
    }

    data object LowPlateletCount: Finding {
        override val findingId = FindingId.LowPlateletCountId
        override val description = "Platelets < 150.000/µl"
    }

    data object MalariaRapidDiagnosticTestPositive: Finding {
        override val findingId = FindingId.MalariaRapidDiagnosticTestPositiveId
        override val description = "Malaria rapid diagnostic test (MRDT) positive"
    }

    data object BloodSmearPositiveForMalaria: Finding {
        override val findingId = FindingId.BloodSmearPositiveForMalariaId
        override val description = "Blood smear (B/S) positive for malaria"
    }

    data object EntamoebaHistolyticaPositive: Finding {
        override val findingId = FindingId.EntamoebaHistolyticaPositiveId
        override val description = "Stool microscopy positive for Entamoeba histolytica"
    }

    data object GiardiaPositive: Finding {
        override val findingId = FindingId.GiardiaPositiveId
        override val description = "Stool microscopy positive for Giardia"
    }

    data object CryptosporidiumPositive: Finding {
        override val findingId = FindingId.CryptosporidiumPositiveId
        override val description = "Stool microscopy positive for Cryptosporidium"
    }

    data object IntestinalWormsPositive: Finding {
        override val findingId = FindingId.IntestinalWormsPositiveId
        override val description = "Stool microscopy positive for intestinal worms"
    }

    data object ToxicMegacolonOrIntestinalObstruction: Finding {
        override val findingId = FindingId.ToxicMegacolonOrIntestinalObstructionId
        override val description = "Abdominal x-ray showing toxic megacolon or intestinal obstruction"
    }

    data object HIVTestPositive: Finding {
        override val findingId = FindingId.HIVTestPositiveId
        override val description = "HIV test positive"
    }

    data object TuberculosisGeneXpertOrMicroscopyPositive: Finding {
        override val findingId = FindingId.TuberculosisGeneXpertOrMicroscopyPositiveId
        override val description = "TB GeneXpert or microscopy positive"
    }

    data object ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia: Finding {
        override val findingId = FindingId.ChestXRayOrPhysicalExaminationSuggestiveOfPneumoniaId
        override val description = "CXR (or physical examination in children) suggestive of pneumonia"
    }

    data object ChestXRaySuggestiveOfPleuralEffusion: Finding {
        override val findingId = FindingId.ChestXRaySuggestiveOfPleuralEffusionId
        override val description = "CXR suggestive of pleural effusion"
    }

    data object ChestXRaySuggestiveOfPneumothorax: Finding {
        override val findingId = FindingId.ChestXRaySuggestiveOfPneumothoraxId
        override val description = "CXR suggestive of PNX"
    }

    data object ChestXRaySuggestiveOfTuberculosis: Finding {
        override val findingId = FindingId.ChestXRaySuggestiveOfTuberculosisId
        override val description = "CXR suggestive of TB"
    }

    data object ChestXRaySuggestiveOfPulmonaryEdema: Finding {
        override val findingId = FindingId.ChestXRaySuggestiveOfPulmonaryEdemaId
        override val description = "CXR suggestive of pulmonary edema"
    }

    data object EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponade: Finding {
        override val findingId = FindingId.EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponadeId
        override val description = "Echocardiography or CXR suggestive of pericardial effusion/tamponade"
    }

    data object EchoCardiographySuggestiveOfMyocardialInfarction: Finding {
        override val findingId = FindingId.EchoCardiographySuggestiveOfMyocardialInfarctionId
        override val description = "ECG suggestive of myocardial infarction"
    }

    data object EchoCardiographySuggestiveOfAtrialFibrillation: Finding {
        override val findingId = FindingId.EchoCardiographySuggestiveOfAtrialFibrillationId
        override val description = "ECG suggestive of atrial fibrillation"
    }

    data object VenousUltraSoundSuggestiveOfDeepVeinThrombosis: Finding {
        override val findingId = FindingId.VenousUltraSoundSuggestiveOfDeepVeinThrombosisId
        override val description = "Venous US suggestive of deep vein thrombosis"
    }

    data object CerebrospinalFluidSuggestiveOfBacterialInfection: Finding {
        override val findingId = FindingId.CerebrospinalFluidSuggestiveOfBacterialInfectionId
        override val description = "CSF suggestive of bacterial infection (cloudy, WBC > 1000 PMN/mm3, glucose < 1.5 mmol/l, proteins > 0.4 g/l or Pandy test positive, Gram +/- organisms)"
    }

    data object CerebrospinalFluidSuggestiveOfTuberculosisMeningitis: Finding {
        override val findingId = FindingId.CerebrospinalFluidSuggestiveOfTuberculosisMeningitisId
        override val description = "CSF suggestive of TB meningitis (TB GeneXpert positive, moderate WBC, mainly lymphocytes, low glucose, high proteins)"
    }

    data object CerebrospinalFluidSuggestiveOfCryptococcalMeningitis: Finding {
        override val findingId = FindingId.CerebrospinalFluidSuggestiveOfCryptococcalMeningitisId
        override val description = "CSF suggestive of cryptococcal meningitis (CragAg positive or India Ink stain positive)"
    }

    data object CerebrospinalFluidSuggestiveOfNeurosyphilis: Finding {
        override val findingId = FindingId.CerebrospinalFluidSuggestiveOfNeurosyphilisId
        override val description = "CSF suggestive of neurosyphilis (CSF VDRL/RPR/TPHA positive)"
    }

    data object SkullXRaySuggestiveOfSkullFracture: Finding {
        override val findingId = FindingId.SkullXRaySuggestiveOfSkullFractureId
        override val description = "Skull x-ray suggestive of skull fracture"
    }

    data object UrineAnalysisWithProteinuria: Finding {
        override val findingId = FindingId.UrineAnalysisWithProteinuriaId
        override val description = "Urine analysis with proteinuria"
    }

    data object CreatinineOrUreaElevated: Finding {
        override val findingId = FindingId.CreatinineOrUreaElevatedId
        override val description = "RFTs (creatinine, urea) elevated"
    }

    data object ASTOrALTOrBilirubinElevated: Finding {
        override val findingId = FindingId.ASTOrALTOrBilirubinElevatedId
        override val description = "LFTs (AST/ALT/bilirubin) elevated"
    }

    data object AlteredElectrolytes: Finding {
        override val findingId = FindingId.AlteredElectrolytesId
        override val description = "Altered electrolytes (Na, K, Ca, Mg)"
    }

    data object PregnancyTestPositive: Finding {
        override val findingId = FindingId.PregnancyTestPositiveId
        override val description = "Pregnancy test positive"
    }
}

sealed class FindingId(
    val value: SnakeCaseString
) {
    object HighWhiteBloodCellsId: FindingId(snakeCase("high_white_blood_cells"))
    object LowHemoglobinId: FindingId(snakeCase("low_hemoglobin"))
    object LowPlateletCountId: FindingId(snakeCase("low_platelet_count"))
    object MalariaRapidDiagnosticTestPositiveId: FindingId(snakeCase("malaria_rapid_diagnostic_test_positive"))
    object BloodSmearPositiveForMalariaId: FindingId(snakeCase("blood_smear_positive_for_malaria"))
    object EntamoebaHistolyticaPositiveId: FindingId(snakeCase("entamoeba_histolytica_positive"))
    object GiardiaPositiveId: FindingId(snakeCase("giardia_positive"))
    object CryptosporidiumPositiveId: FindingId(snakeCase("cryptosporidium_positive"))
    object IntestinalWormsPositiveId: FindingId(snakeCase("intestinal_worms_positive"))
    object ToxicMegacolonOrIntestinalObstructionId: FindingId(snakeCase("toxic_megacolon_or_intestinal_obstruction"))
    object HIVTestPositiveId: FindingId(snakeCase("hiv_test_positive"))
    object TuberculosisGeneXpertOrMicroscopyPositiveId: FindingId(snakeCase("tuberculosis_gene_xpert_or_microscopy_positive"))
    object ChestXRayOrPhysicalExaminationSuggestiveOfPneumoniaId: FindingId(snakeCase("chest_xray_or_physical_examination_suggestive_of_pneumonia"))
    object ChestXRaySuggestiveOfPleuralEffusionId: FindingId(snakeCase("chest_xray_suggestive_of_pleural_effusion"))
    object ChestXRaySuggestiveOfPneumothoraxId: FindingId(snakeCase("chest_xray_suggestive_of_pneumothorax"))
    object ChestXRaySuggestiveOfTuberculosisId: FindingId(snakeCase("chest_xray_suggestive_of_tuberculosis"))
    object ChestXRaySuggestiveOfPulmonaryEdemaId: FindingId(snakeCase("chest_xray_suggestive_of_pulmonary_edema"))
    object EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponadeId: FindingId(snakeCase("echocardiography_or_chest_xray_suggestive_of_pericardial_effusion_tamponade"))
    object EchoCardiographySuggestiveOfMyocardialInfarctionId: FindingId(snakeCase("echo_cardiography_suggestive_of_myocardial_infarction"))
    object EchoCardiographySuggestiveOfAtrialFibrillationId: FindingId(snakeCase("echo_cardiography_suggestive_of_atrial_fibrillation"))
    object VenousUltraSoundSuggestiveOfDeepVeinThrombosisId: FindingId(snakeCase("venous_ultra_sound_suggestive_of_deep_vein_thrombosis"))
    object CerebrospinalFluidSuggestiveOfBacterialInfectionId: FindingId(snakeCase("cerebrospinal_fluid_suggestive_of_bacterial_infection"))
    object CerebrospinalFluidSuggestiveOfTuberculosisMeningitisId: FindingId(snakeCase("cerebrospinal_fluid_suggestive_of_tuberculosis_meningitis"))
    object CerebrospinalFluidSuggestiveOfCryptococcalMeningitisId: FindingId(snakeCase("cerebrospinal_fluid_suggestive_of_cryptococcal_meningitis"))
    object CerebrospinalFluidSuggestiveOfNeurosyphilisId: FindingId(snakeCase("cerebrospinal_fluid_suggestive_of_neurosyphilis"))
    object SkullXRaySuggestiveOfSkullFractureId: FindingId(snakeCase("skull_xray_suggestive_of_skull_fracture"))
    object UrineAnalysisWithProteinuriaId: FindingId(snakeCase("urine_analysis_with_proteinuria"))
    object CreatinineOrUreaElevatedId: FindingId(snakeCase("creatinine_or_urea_elevated"))
    object ASTOrALTOrBilirubinElevatedId: FindingId(snakeCase("ast_or_alt_or_bilirubin_elevated"))
    object AlteredElectrolytesId: FindingId(snakeCase("altered_electrolytes"))
    object PregnancyTestPositiveId: FindingId(snakeCase("pregnancy_test_positive"))
}
