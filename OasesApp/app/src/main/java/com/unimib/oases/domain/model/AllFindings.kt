package com.unimib.oases.domain.model

import com.unimib.oases.domain.model.complaint.Finding
import com.unimib.oases.domain.model.complaint.Finding.ToxicMegacolonOrIntestinalObstruction

/**
 * Single source of truth for all Findings.
 * Use `allFindings` whenever you need to iterate, validate, or reference all available findings.
 */
val allFindings: Set<Finding> = setOf(
    Finding.HighWhiteBloodCells,
    Finding.LowHemoglobin,
    Finding.LowPlateletCount,
    Finding.MalariaRapidDiagnosticTestPositive,
    Finding.BloodSmearPositiveForMalaria,
    Finding.EntamoebaHistolyticaPositive,
    Finding.GiardiaPositive,
    Finding.CryptosporidiumPositive,
    Finding.IntestinalWormsPositive,
    ToxicMegacolonOrIntestinalObstruction,
    Finding.HIVTestPositive,
    Finding.TuberculosisGeneXpertOrMicroscopyPositive,
    Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia,
    Finding.ChestXRaySuggestiveOfPleuralEffusion,
    Finding.ChestXRaySuggestiveOfPneumothorax,
    Finding.ChestXRaySuggestiveOfTuberculosis,
    Finding.ChestXRaySuggestiveOfPulmonaryEdema,
    Finding.EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponade,
    Finding.EchoCardiographySuggestiveOfMyocardialInfarction,
    Finding.EchoCardiographySuggestiveOfAtrialFibrillation,
    Finding.VenousUltraSoundSuggestiveOfDeepVeinThrombosis,
    Finding.CerebrospinalFluidSuggestiveOfBacterialInfection,
    Finding.CerebrospinalFluidSuggestiveOfTuberculosisMeningitis,
    Finding.CerebrospinalFluidSuggestiveOfCryptococcalMeningitis,
    Finding.CerebrospinalFluidSuggestiveOfNeurosyphilis,
    Finding.SkullXRaySuggestiveOfSkullFracture,
    Finding.UrineAnalysisWithProteinuria,
    Finding.CreatinineOrUreaElevated,
    Finding.ASTOrALTOrBilirubinElevated,
    Finding.AlteredElectrolytes,
    Finding.PregnancyTestPositive
)

/**
 * Map for quick lookup by symptomId
 */
val findingsById: Map<String, Finding> = allFindings.associateBy { it.id }