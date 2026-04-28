package com.unimib.oases.domain.model.complaint

object SeizuresOrComaDefinitiveTherapies: ComplaintDefinitiveTherapies {
    val complaintId = ComplaintId.SEIZURES_OR_COMA
    override val definitiveTherapies = setOf(
        DefinitiveTherapy.Malaria(complaintId),
        DefinitiveTherapy.SuspectedOrConfirmedBacterialMeningitis,
        DefinitiveTherapy.TuberculosisMeningitis,
        DefinitiveTherapy.CryptoCoccalMeningitis,
        DefinitiveTherapy.Neurosyphilis,
        DefinitiveTherapy.HIVInfection(complaintId),
        DefinitiveTherapy.Epilepsy,
        DefinitiveTherapy.SuspectedEclampsiaForPregnancyBeyondTwentyWeeksAndSeizures,
        DefinitiveTherapy.HeadTrauma,
        DefinitiveTherapy.PossibleAlcoholAbuse,
        DefinitiveTherapy.PossibleStrokeOrPossibleIntracranialHemorrhage,
        DefinitiveTherapy.SuspectedPoisoningOrSuspectedIntoxication,
        DefinitiveTherapy.RenalFailureAndPossibleUremicEncephalopathy,
        DefinitiveTherapy.LiverFailureAndPossibleHepaticEncephalopathy,
        DefinitiveTherapy.ElectrolytesAlterations,
        DefinitiveTherapy.LowHemoglobin,
        DefinitiveTherapy.SeizuresOrComaHighRiskPatientHospitalization
    )
}