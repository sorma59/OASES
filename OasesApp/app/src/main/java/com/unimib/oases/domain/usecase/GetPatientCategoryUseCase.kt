package com.unimib.oases.domain.usecase

import com.unimib.oases.ui.screen.patient_registration.triage.PatientCategory
import com.unimib.oases.util.AppConstants
import javax.inject.Inject

class GetPatientCategoryUseCase @Inject constructor() {
    operator fun invoke(ageInMonths: Int): PatientCategory {
        return if ((ageInMonths / 12 ) < AppConstants.MATURITY_AGE) {
            PatientCategory.PEDIATRIC
        } else {
            PatientCategory.ADULT
        }
    }
}