package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Finding.ASTOrALTOrBilirubinElevated
import com.unimib.oases.domain.model.complaint.Finding.AlteredElectrolytes
import com.unimib.oases.domain.model.complaint.Finding.BloodSmearPositiveForMalaria
import com.unimib.oases.domain.model.complaint.Finding.CerebrospinalFluidSuggestiveOfBacterialInfection
import com.unimib.oases.domain.model.complaint.Finding.CerebrospinalFluidSuggestiveOfCryptococcalMeningitis
import com.unimib.oases.domain.model.complaint.Finding.CerebrospinalFluidSuggestiveOfNeurosyphilis
import com.unimib.oases.domain.model.complaint.Finding.CerebrospinalFluidSuggestiveOfTuberculosisMeningitis
import com.unimib.oases.domain.model.complaint.Finding.CreatinineOrUreaElevated
import com.unimib.oases.domain.model.complaint.Finding.HIVTestPositive
import com.unimib.oases.domain.model.complaint.Finding.HighWhiteBloodCells
import com.unimib.oases.domain.model.complaint.Finding.LowHemoglobin
import com.unimib.oases.domain.model.complaint.Finding.LowPlateletCount
import com.unimib.oases.domain.model.complaint.Finding.MalariaRapidDiagnosticTestPositive
import com.unimib.oases.domain.model.complaint.Finding.SkullXRaySuggestiveOfSkullFracture
import com.unimib.oases.domain.model.complaint.Finding.TuberculosisGeneXpertOrMicroscopyPositive
import com.unimib.oases.domain.model.complaint.Finding.UrineAnalysisWithProteinuria

object SeizuresOrComaFindings: ComplaintFindings {
    override val findings = setOf(
        HighWhiteBloodCells,
        LowHemoglobin,
        LowPlateletCount,
        MalariaRapidDiagnosticTestPositive,
        BloodSmearPositiveForMalaria,
        HIVTestPositive,
        TuberculosisGeneXpertOrMicroscopyPositive,
        CerebrospinalFluidSuggestiveOfBacterialInfection,
        CerebrospinalFluidSuggestiveOfTuberculosisMeningitis,
        CerebrospinalFluidSuggestiveOfCryptococcalMeningitis,
        CerebrospinalFluidSuggestiveOfNeurosyphilis,
        SkullXRaySuggestiveOfSkullFracture,
        UrineAnalysisWithProteinuria,
        CreatinineOrUreaElevated,
        ASTOrALTOrBilirubinElevated,
        AlteredElectrolytes
    )
}