package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Finding.BloodSmearPositiveForMalaria
import com.unimib.oases.domain.model.complaint.Finding.HIVTestPositive
import com.unimib.oases.domain.model.complaint.Finding.HighWhiteBloodCells
import com.unimib.oases.domain.model.complaint.Finding.LowHemoglobin
import com.unimib.oases.domain.model.complaint.Finding.LowPlateletCount
import com.unimib.oases.domain.model.complaint.Finding.MalariaRapidDiagnosticTestPositive
import com.unimib.oases.domain.model.complaint.Finding.TuberculosisGeneXpertOrMicroscopyPositive

object OtherFindings: ComplaintFindings {
    override val findings = setOf(
        HighWhiteBloodCells,
        LowHemoglobin,
        LowPlateletCount,
        MalariaRapidDiagnosticTestPositive,
        BloodSmearPositiveForMalaria,
        HIVTestPositive,
        TuberculosisGeneXpertOrMicroscopyPositive,
        Finding.PregnancyTestPositive,
    )
}