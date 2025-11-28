package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardAction
import javax.inject.Inject

class ConfigPatientDashboardActionsUseCase @Inject constructor(
    private val getCurrentRoleUseCase: GetCurrentRoleUseCase
) {
    operator fun invoke(patientId: String): List<PatientDashboardAction> {
        val buttons = PatientDashboardAction.entries.filter {
            it.roles.contains(getCurrentRoleUseCase())
        }
        return buttons
    }
}