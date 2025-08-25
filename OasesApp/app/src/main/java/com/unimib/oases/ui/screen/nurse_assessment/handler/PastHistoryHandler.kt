package com.unimib.oases.ui.screen.nurse_assessment.handler

import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PastHistoryEffect
import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PastHistoryEvent
import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PastHistoryState
import javax.inject.Inject

class PastHistoryHandler @Inject constructor() {
    fun handle(
        state: PastHistoryState,
        event: PastHistoryEvent
    ): Pair<PastHistoryState, PastHistoryEffect?> {
        return when (event) {
            is PastHistoryEvent.AdditionalInfoChanged -> {
                return state.copy(
                    diseases = state.diseases.map { d ->
                        if (d.disease == event.disease)
                            d.copy(additionalInfo = event.additionalInfo)
                        else
                            d
                    }
                ) to null
            }
            is PastHistoryEvent.DateChanged -> {
                return state.copy(
                    diseases = state.diseases.map { d ->
                        if (d.disease == event.disease)
                            d.copy(date = event.date)
                        else
                            d
                    }
                ) to null
            }
            is PastHistoryEvent.NurseClicked -> {
                return state.copy(
                    toastMessage = "Only doctors can edit PMH"
                ) to null
            }
            is PastHistoryEvent.RadioButtonClicked -> {
                return state.copy(
                    diseases = state.diseases.map { d ->
                        if (d.disease == event.disease)
                            d.copy(isDiagnosed = event.isDiagnosed)
                        else
                            d
                    }
                ) to null
            }
            is PastHistoryEvent.Retry -> {
                state.copy(
                    isLoading = true,
                    error = null
                ) to PastHistoryEffect.Refresh
            }
            is PastHistoryEvent.ToastShown -> {
                return state.copy(
                    toastMessage = null
                ) to null
            }
        }
    }
}
