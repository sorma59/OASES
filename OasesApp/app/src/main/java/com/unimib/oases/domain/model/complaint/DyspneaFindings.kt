package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Finding.BloodSmearPositiveForMalaria
import com.unimib.oases.domain.model.complaint.Finding.ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia
import com.unimib.oases.domain.model.complaint.Finding.ChestXRaySuggestiveOfPleuralEffusion
import com.unimib.oases.domain.model.complaint.Finding.ChestXRaySuggestiveOfPneumothorax
import com.unimib.oases.domain.model.complaint.Finding.ChestXRaySuggestiveOfPulmonaryEdema
import com.unimib.oases.domain.model.complaint.Finding.ChestXRaySuggestiveOfTuberculosis
import com.unimib.oases.domain.model.complaint.Finding.EchoCardiographySuggestiveOfAtrialFibrillation
import com.unimib.oases.domain.model.complaint.Finding.EchoCardiographySuggestiveOfMyocardialInfarction
import com.unimib.oases.domain.model.complaint.Finding.EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponade
import com.unimib.oases.domain.model.complaint.Finding.HIVTestPositive
import com.unimib.oases.domain.model.complaint.Finding.HighWhiteBloodCells
import com.unimib.oases.domain.model.complaint.Finding.LowHemoglobin
import com.unimib.oases.domain.model.complaint.Finding.LowPlateletCount
import com.unimib.oases.domain.model.complaint.Finding.MalariaRapidDiagnosticTestPositive
import com.unimib.oases.domain.model.complaint.Finding.TuberculosisGeneXpertOrMicroscopyPositive
import com.unimib.oases.domain.model.complaint.Finding.VenousUltraSoundSuggestiveOfDeepVeinThrombosis

object DyspneaFindings: ComplaintFindings {
    override val findings = setOf(
        HighWhiteBloodCells,
        LowHemoglobin,
        LowPlateletCount,
        MalariaRapidDiagnosticTestPositive,
        BloodSmearPositiveForMalaria,
        HIVTestPositive,
        TuberculosisGeneXpertOrMicroscopyPositive,
        ChestXRayOrPhysicalExaminationSuggestiveOfPneumonia,
        ChestXRaySuggestiveOfPleuralEffusion,
        ChestXRaySuggestiveOfPneumothorax,
        ChestXRaySuggestiveOfTuberculosis,
        ChestXRaySuggestiveOfPulmonaryEdema,
        EchocardiographyOrChestXRaySuggestiveOfPericardialEffusionTamponade,
        EchoCardiographySuggestiveOfMyocardialInfarction,
        EchoCardiographySuggestiveOfAtrialFibrillation,
        VenousUltraSoundSuggestiveOfDeepVeinThrombosis
    )
}