package com.unimib.oases.domain.model.complaint

object SeizuresOrComaDefinitiveTherapies: ComplaintDefinitiveTherapies {
    override val definitiveTherapies = setOf(
        DefinitiveTherapy.Malaria(ComplaintId.SEIZURES_OR_COMA),
        DefinitiveTherapy.SuspectedOrConfirmedBacterialMeningitis,
        DefinitiveTherapy.TuberculosisMeningitis,
        DefinitiveTherapy.CryptoCoccalMeningitis,
        DefinitiveTherapy.Neurosyphilis,
        DefinitiveTherapy.HIVInfection(ComplaintId.SEIZURES_OR_COMA),
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