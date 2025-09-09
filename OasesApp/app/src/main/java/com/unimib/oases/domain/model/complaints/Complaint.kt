package com.unimib.oases.domain.model.complaints

sealed interface Complaint {
    val id: ComplaintId
}

enum class ComplaintId(val id: String) {
    DIARRHEA("diarrhea")
}