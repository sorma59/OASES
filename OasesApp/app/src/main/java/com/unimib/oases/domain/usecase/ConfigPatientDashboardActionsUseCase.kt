package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.auth.AuthManager
import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardAction
import javax.inject.Inject

class ConfigPatientDashboardActionsUseCase @Inject constructor(
    private val authManager: AuthManager
) {
    operator fun invoke(patientId: String): List<PatientDashboardAction> {
        val buttons = PatientDashboardAction.entries.filter {
            it.roles.contains(authManager.getCurrentRole())
        }
        return buttons
    }
}