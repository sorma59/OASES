package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.nurse_assessment.demographics.PatientData
import com.unimib.oases.ui.screen.nurse_assessment.demographics.toModel
import com.unimib.oases.util.Outcome
import kotlinx.coroutines.delay
import javax.inject.Inject

class SavePatientDataUseCase @Inject constructor(
    private val insertPatientUseCase: InsertPatientUseCase
) {
    suspend operator fun invoke(patientData: PatientData): Outcome<String> {
        delay(500)
        return insertPatientUseCase(patientData.toModel())
    }
}