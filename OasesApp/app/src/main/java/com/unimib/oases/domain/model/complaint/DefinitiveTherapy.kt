package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom

sealed interface DefinitiveTherapy {

    val explanation: String
    val predicate: (Set<Symptom>, Set<Finding>) -> Boolean


}