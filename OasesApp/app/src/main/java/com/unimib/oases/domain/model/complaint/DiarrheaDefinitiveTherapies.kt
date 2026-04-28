package com.unimib.oases.domain.model.complaint

object DiarrheaDefinitiveTherapies: ComplaintDefinitiveTherapies {
    val complaintId = ComplaintId.DIARRHEA
    override val definitiveTherapies = setOf(
        DefinitiveTherapy.BacterialInfection,
        DefinitiveTherapy.CholeraInfection,
        DefinitiveTherapy.EntamoebaHistolyticaInfection,
        DefinitiveTherapy.GiardiaInfection,
        DefinitiveTherapy.CryptosporidiumInfection,
        DefinitiveTherapy.IntestinalWorms,
        DefinitiveTherapy.Malaria(complaintId),
        DefinitiveTherapy.ToxicMegacolonOrIntestinalObstruction,
        DefinitiveTherapy.HIVInfection(complaintId),
        DefinitiveTherapy.HemolyticUremicSyndrome,
        DefinitiveTherapy.LowHemoglobin,
        DefinitiveTherapy.DiarrheaHighRiskPatientHospitalization
    )
}