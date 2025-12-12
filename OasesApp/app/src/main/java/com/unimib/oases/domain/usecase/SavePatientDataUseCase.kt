package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.nurse_assessment.demographics.PatientData
import com.unimib.oases.ui.screen.nurse_assessment.demographics.toModel
import com.unimib.oases.util.Outcome
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class SavePatientDataUseCase @Inject constructor(
    private val insertPatientUseCase: InsertPatientUseCase
) {
    suspend operator fun invoke(patientData: PatientData): Outcome<String> {
        delay(500)
        if (Random.nextBoolean()) //TODO("DEBUG ONLY: REMOVE")
            return Outcome.Error("Mock error")
        return insertPatientUseCase(patientData.toModel())
    }
}