package com.unimib.oases.domain.model.complaint

class DiarrheaDefinitiveTherapies(
    complaintId: ComplaintId
): ComplaintDefinitiveTherapies {
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