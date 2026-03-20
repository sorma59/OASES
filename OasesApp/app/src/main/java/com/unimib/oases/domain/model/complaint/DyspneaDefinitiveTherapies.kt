package com.unimib.oases.domain.model.complaint

object DyspneaDefinitiveTherapies: ComplaintDefinitiveTherapies {
    override val definitiveTherapies = setOf(
        DefinitiveTherapy.SeverePneumonia,
        DefinitiveTherapy.NonSeverePneumonia,
        DefinitiveTherapy.PleuralEffusion,
        DefinitiveTherapy.Pneumothorax,
        DefinitiveTherapy.PresumptiveOrConfirmedTuberculosis,
        DefinitiveTherapy.PulmonaryEdema,
        DefinitiveTherapy.PericardialEffusionOrPericardialTamponade,
        DefinitiveTherapy.MyocardialInfarction,
        DefinitiveTherapy.AtrialFibrillation,
        DefinitiveTherapy.DeepVeinThrombosisOrSuspectedPulmonaryEmbolism,
        DefinitiveTherapy.SuspectedSevereAcuteAsthma,
        DefinitiveTherapy.SuspectedAcuteAsthma,
        DefinitiveTherapy.SuspectedChronicObstructivePulmonaryDiseaseExacerbation,
        DefinitiveTherapy.SuspectedBronchiolitis,
        DefinitiveTherapy.SuspectedPertussis,
        DefinitiveTherapy.SickleCellDiseaseAndSuspectedAcuteChestSyndrome,
        DefinitiveTherapy.StridorAndNoForeignBodyObstruction,
        DefinitiveTherapy.Malaria(ComplaintId.DYSPNEA),
        DefinitiveTherapy.HIVInfection(ComplaintId.DYSPNEA),
        DefinitiveTherapy.LowHemoglobin,
        DefinitiveTherapy.DyspneaHighRiskPatientHospitalization
    )
}