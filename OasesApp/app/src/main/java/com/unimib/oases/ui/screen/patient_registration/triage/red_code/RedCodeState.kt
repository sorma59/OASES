package com.unimib.oases.ui.screen.patient_registration.triage.red_code

data class RedCodeState(
    var unconsciousness: Boolean = false,
    var activeConvulsions: Boolean = false,
    var respiratoryDistress: Boolean = false,
    var heavyBleeding: Boolean = false,
    var highRiskTraumaBurns: Boolean = false,
    var threatenedLimb: Boolean = false,
    var poisoningIntoxication: Boolean = false,
    var snakeBite: Boolean = false,
    var aggressiveBehavior: Boolean = false,
    var pregnancyHeavyBleeding: Boolean = false,
    var severeAbdominalPain: Boolean = false,
    var seizures: Boolean = false,
    var alteredMentalStatus: Boolean = false,
    var severeHeadache: Boolean = false,
    var visualChanges: Boolean = false,
    var sbpHighDpbHigh: Boolean = false,
    var trauma: Boolean = false,
    var activeLabor: Boolean = false,
)
