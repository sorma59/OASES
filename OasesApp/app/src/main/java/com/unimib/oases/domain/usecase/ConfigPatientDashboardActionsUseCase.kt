package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardAction
import com.unimib.oases.util.firstNullableSuccess
import javax.inject.Inject

class ConfigPatientDashboardActionsUseCase @Inject constructor(
    private val getCurrentRoleUseCase: GetCurrentRoleUseCase,
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase
) {
    suspend operator fun invoke(patientId: String): List<PatientDashboardAction> {
        val buttons = PatientDashboardAction.entries.filter {
            it.roles.contains(getCurrentRoleUseCase())
        }
        val currentVisit = getCurrentVisitUseCase(patientId).firstNullableSuccess()
        return if (currentVisit == null)
            buttons.minus(PatientDashboardAction.StartVisit)
        else
            buttons
    }
}