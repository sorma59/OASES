package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.complaint.Finding.BloodSmearPositiveForMalaria
import com.unimib.oases.domain.model.complaint.Finding.CryptosporidiumPositive
import com.unimib.oases.domain.model.complaint.Finding.EntamoebaHistolyticaPositive
import com.unimib.oases.domain.model.complaint.Finding.GiardiaPositive
import com.unimib.oases.domain.model.complaint.Finding.HIVTestPositive
import com.unimib.oases.domain.model.complaint.Finding.HighWhiteBloodCells
import com.unimib.oases.domain.model.complaint.Finding.IntestinalWormsPositive
import com.unimib.oases.domain.model.complaint.Finding.LowHemoglobin
import com.unimib.oases.domain.model.complaint.Finding.LowPlateletCount
import com.unimib.oases.domain.model.complaint.Finding.MalariaRapidDiagnosticTestPositive
import com.unimib.oases.domain.model.complaint.Finding.ToxicMegacolonOrIntestinalObstruction

object DiarrheaFindings: ComplaintFindings {
    override val findings = setOf(
        HighWhiteBloodCells,
        LowHemoglobin,
        LowPlateletCount,
        MalariaRapidDiagnosticTestPositive,
        BloodSmearPositiveForMalaria,
        EntamoebaHistolyticaPositive,
        GiardiaPositive,
        CryptosporidiumPositive,
        IntestinalWormsPositive,
        ToxicMegacolonOrIntestinalObstruction,
        HIVTestPositive
    )
}