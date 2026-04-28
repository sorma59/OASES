package com.unimib.oases.domain.model.complaint

object OtherDefinitiveTherapies: ComplaintDefinitiveTherapies {
    val complaintId = ComplaintId.OTHER
    override val definitiveTherapies = setOf(
        DefinitiveTherapy.LowHemoglobin,
        DefinitiveTherapy.Malaria(complaintId),
        DefinitiveTherapy.HIVInfection(complaintId),
        DefinitiveTherapy.ConfirmedTuberculosis,
        DefinitiveTherapy.PregnancyTestPositive,
        DefinitiveTherapy.OtherHighRiskPatientHospitalization,
    )
}