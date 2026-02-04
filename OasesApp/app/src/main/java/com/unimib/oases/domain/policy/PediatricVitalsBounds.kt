package com.unimib.oases.domain.policy

data class PediatricVitalBounds(
    val rrLower: Int,
    val rrUpper: Int,
    val hrLower: Int,
    val hrUpper: Int,
    val extraSymptoms: Set<String> = emptySet()
)