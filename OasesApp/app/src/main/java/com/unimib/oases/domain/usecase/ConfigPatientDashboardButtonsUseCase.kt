package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardButton
import javax.inject.Inject

class ConfigPatientDashboardButtonsUseCase @Inject constructor(
    private val getCurrentRoleUseCase: GetCurrentRoleUseCase
) {
    operator fun invoke(): List<PatientDashboardButton> {
        return PatientDashboardButton.entries.filter {
            it.roles.contains(getCurrentRoleUseCase())
        }
    }
}