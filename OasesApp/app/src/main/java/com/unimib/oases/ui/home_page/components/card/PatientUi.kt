package com.unimib.oases.ui.home_page.components.card

data class PatientUi(
    val id: Int,
    val name: String,
    val isOptionsRevealed: Boolean,
    val lastVisit: String = "05/05/2025",
    val state: String = "G"
)
