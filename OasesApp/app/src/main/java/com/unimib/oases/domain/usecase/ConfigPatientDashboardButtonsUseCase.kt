package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardButton
import com.unimib.oases.util.Resource
import javax.inject.Inject

class ConfigPatientDashboardButtonsUseCase @Inject constructor(
    private val getCurrentRoleUseCase: GetCurrentRoleUseCase,
    private val getCurrentVisitUseCase: GetCurrentVisitUseCase
) {
    suspend operator fun invoke(patientId: String): List<PatientDashboardButton> {
        val buttons = PatientDashboardButton.entries.filter {
            it.roles.contains(getCurrentRoleUseCase())
        }
        val currentVisit = getCurrentVisitUseCase(patientId)
        return if (currentVisit is Resource.Error || currentVisit.data == null)
            buttons.minus(PatientDashboardButton.START_VISIT)
        else
            buttons
    }
}