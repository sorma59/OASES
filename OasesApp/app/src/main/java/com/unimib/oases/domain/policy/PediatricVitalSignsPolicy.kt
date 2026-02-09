package com.unimib.oases.domain.policy

import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.HR_LOW_FOR_ONE_YEAR_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_HIGH_FOR_ONE_YEAR_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS
import com.unimib.oases.domain.model.symptom.TriageSymptom.Companion.RR_LOW_FOR_ONE_YEAR_OLDS
import javax.inject.Inject

class PediatricVitalSignsPolicy @Inject constructor() {
    fun pediatricBounds(ageInMonths: Int): PediatricVitalBounds? {
        return when (ageInMonths / 12) {
            0 -> PediatricVitalBounds(
                rrLower = RR_LOW_FOR_ONE_YEAR_OLDS,
                rrUpper = RR_HIGH_FOR_ONE_YEAR_OLDS,
                hrLower = HR_LOW_FOR_ONE_YEAR_OLDS,
                hrUpper = HR_HIGH_FOR_ONE_YEAR_OLDS,
                extraSymptoms = if (ageInMonths < 6)
                    setOf(TriageSymptom.YOUNGER_THAN_SIX_MONTHS.id)
                else emptySet()
            )

            in 1..4 -> PediatricVitalBounds(
                rrLower = RR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS,
                rrUpper = RR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS,
                hrLower = HR_LOW_FOR_ONE_TO_FOUR_YEARS_OLDS,
                hrUpper = HR_HIGH_FOR_ONE_TO_FOUR_YEARS_OLDS
            )

            in 5..12 -> PediatricVitalBounds(
                rrLower = RR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS,
                rrUpper = RR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS,
                hrLower = HR_LOW_FOR_FIVE_TO_TWELVE_YEARS_OLDS,
                hrUpper = HR_HIGH_FOR_FIVE_TO_TWELVE_YEARS_OLDS
            )

            else -> null
        }
    }
}