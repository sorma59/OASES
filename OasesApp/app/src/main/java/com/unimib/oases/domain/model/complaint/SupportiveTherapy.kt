package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom

sealed class SupportiveTherapy(
    val text: String,
    val predicate: (Set<Symptom>) -> Boolean
)

