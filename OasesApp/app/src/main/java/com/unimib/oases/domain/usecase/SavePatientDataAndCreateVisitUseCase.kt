package com.unimib.oases.domain.usecase

import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.domain.model.Visit
import com.unimib.oases.domain.repository.PatientRepository
import com.unimib.oases.ui.screen.nurse_assessment.demographics.PatientData
import com.unimib.oases.ui.screen.nurse_assessment.demographics.toModel
import com.unimib.oases.util.DateAndTimeUtils
import com.unimib.oases.util.Outcome
import javax.inject.Inject

class SavePatientDataAndCreateVisitUseCase @Inject constructor(
    private val patientRepository: PatientRepository,
) {
    suspend operator fun invoke(patientData: PatientData): Outcome<PatientAndVisitIds> {
        val patient = patientData.toModel()
        // Wizard: visit needs to be created
        val visit = Visit(
            patientId = patient.id,
            arrivalTime = DateAndTimeUtils.getCurrentTime(),
            date = DateAndTimeUtils.getCurrentDate()
        )
        return patientRepository.addPatientAndCreateVisit(patient, visit)
    }
}