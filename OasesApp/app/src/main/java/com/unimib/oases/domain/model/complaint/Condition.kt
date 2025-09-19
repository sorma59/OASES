package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom

sealed interface Condition{
    val label: String
    val predicate: (Set<Symptom>) -> Boolean
    val suggestedTests: List<Test>
}

