package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.domain.repository.VisitVitalSignRepository
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form.VitalSignsFormState
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SaveVisitVitalSignsUseCase @Inject constructor(
    private val repo: VisitVitalSignRepository
) {
    suspend operator fun invoke(state: VitalSignsFormState): Outcome<Unit> {
        return repo.addVisitVitalSigns(state.toVisitVitalSigns())
    }

    private fun VitalSignsFormState.toVisitVitalSigns(): List<VisitVitalSign> {

        return vitalSigns.mapNotNull {
            val numericValue = it.value.toDoubleOrNull() ?: return@mapNotNull null

            VisitVitalSign(
                visitId = visitId,
                vitalSignName = it.name,
                timestamp = System.currentTimeMillis().toString(),
                value = numericValue,
            )
        }
    }
}