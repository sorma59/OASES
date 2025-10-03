package com.unimib.oases.domain.model.complaint

@JvmInline
value class ImmediateTreatment (val text: String) {
    override fun toString(): String = text
}