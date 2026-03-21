package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.complaint.DefinitiveTherapy
import com.unimib.oases.domain.model.complaint.TherapyEvaluationParameters
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.ui.screen.medical_visit.reassessment.ReassessmentState
import com.unimib.oases.util.firstSuccess
import javax.inject.Inject

class GenerateDefinitiveTherapiesUseCase @Inject constructor(
    private val visitVitalSignsRepository: VisitVitalSignRepository,
) {
    suspend operator fun invoke(state: ReassessmentState): Set<DefinitiveTherapy> {
        val vitalSigns = visitVitalSignsRepository
            .getVisitLatestVitalSigns(state.visitId)
            .firstSuccess()
        return state.complaint?.definitiveTherapies?.definitiveTherapies?.filter {
            it.predicate(
                TherapyEvaluationParameters(
                    symptoms = state.symptoms,
                    findings = state.findings,
                    ageInMonths = state.patient?.ageInMonths,
                    sbp = vitalSigns.find { sign -> sign.vitalSignName == "Systolic Blood Pressure" }?.value?.toInt(),
                    dbp = vitalSigns.find { sign -> sign.vitalSignName == "Diastolic Blood Pressure" }?.value?.toInt(),
                )
            )
        }.orEmpty().toSet()
    }
}