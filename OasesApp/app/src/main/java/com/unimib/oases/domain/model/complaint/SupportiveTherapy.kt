package com.unimib.oases.domain.model.complaint

import com.unimib.oases.domain.model.symptom.Symptom
import kotlinx.serialization.Serializable

sealed class SupportiveTherapy(
    val therapy: SupportiveTherapyText,
    val predicate: (Set<Symptom>) -> Boolean
){
    val text: String
        get() = therapy.text
}

@JvmInline
@Serializable
value class SupportiveTherapyText(val text: String)

