package com.unimib.oases.domain.model.complaint

object DiarrheaDefinitiveTherapies: ComplaintDefinitiveTherapies {
    override val definitiveTherapies = setOf(
        DefinitiveTherapy.BacterialInfection,
        DefinitiveTherapy.CholeraInfection,
        DefinitiveTherapy.EntamoebaHistolyticaInfection,
        DefinitiveTherapy.GiardiaInfection,
        DefinitiveTherapy.CryptosporidiumInfection,
        DefinitiveTherapy.IntestinalWorms,
        DefinitiveTherapy.Malaria(ComplaintId.DIARRHEA),
        DefinitiveTherapy.ToxicMegacolonOrIntestinalObstruction,
        DefinitiveTherapy.HIVInfection(ComplaintId.DIARRHEA),
        DefinitiveTherapy.HemolyticUremicSyndrome,
        DefinitiveTherapy.LowHemoglobin,
        DefinitiveTherapy.DiarrheaHighRiskPatientHospitalization
    )
}