package com.unimib.oases.ui.screen.past_visit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.nurse_assessment.demographics.DemographicsSummary
import com.unimib.oases.ui.screen.nurse_assessment.history.PastHistorySummary
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningSummary
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageCard
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun PastVisitSummaryScreen(appViewModel: AppViewModel) {
    val viewModel: PastVisitSummaryViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    PastVisitSummaryContent(state, viewModel::onEvent)
}

@Composable
private fun PastVisitSummaryContent(
    state: PastVisitSummaryState,
    onEvent: (PastVisitSummaryEvent) -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        DemographicsSummary(state.patientData, { }, hasEditButton = false)

        TriageCard(state.triageData, {}, hasEditButton = false)

        MalnutritionScreeningSummary(state.malnutritionData, { }, hasEditButton = false)

        PastHistorySummary(
            freeTextDiseases = state.chronicDiseasesData.first,
            selectionDiseases = state.chronicDiseasesData.second,
            onEvent = {},
            hasEditButton = false,
        )
    }

}