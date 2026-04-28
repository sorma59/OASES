package com.unimib.oases.domain.model.complaint

object DyspneaDefinitiveTherapies: ComplaintDefinitiveTherapies {
    val complaintId = ComplaintId.DYSPNEA
    override val definitiveTherapies = setOf(
        DefinitiveTherapy.SeverePneumonia,
        DefinitiveTherapy.NonSeverePneumonia,
        DefinitiveTherapy.PleuralEffusion,
        DefinitiveTherapy.Pneumothorax,
        DefinitiveTherapy.PresumptiveOrConfirmedTuberculosis(complaintId),
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
        DefinitiveTherapy.Malaria(complaintId),
        DefinitiveTherapy.HIVInfection(complaintId),
        DefinitiveTherapy.LowHemoglobin,
        DefinitiveTherapy.DyspneaHighRiskPatientHospitalization
    )
}