package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.tab.TabSwitcher
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.button.StartButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun HistoryScreen(
    appViewModel: AppViewModel
) {

    val viewModel: HistoryViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.pastMedicalHistoryState.isLoading || state.pastVisitsState.isLoading)

    HistoryContent(
        state,
        viewModel::onEvent,
        viewModel::shouldShowCreateButton,
        viewModel::shouldShowEditButton,
        Modifier.padding(16.dp)
    )

}

@Composable
private fun HistoryContent(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit,
    shouldShowCreateButton: () -> Boolean,
    shouldShowEditButton: () -> Boolean,
    modifier: Modifier = Modifier
) {

    @Composable
    fun GetPastMedicalHistoryContent() {

        state.pastMedicalHistoryState.error?.let {
            RetryButton(
                error = it,
                onClick = { onEvent(HistoryEvent.ReloadPastMedicalHistory) }
            )
        } ?: when (state.pastMedicalHistoryState.mode) {
            is PmhMode.View -> {
                if (state.pastMedicalHistoryState.isPastMedicalHistoryPresent)
                    PastHistorySummary(
                        state.pastMedicalHistoryState.mode.freeTextDiseases,
                        state.pastMedicalHistoryState.mode.selectionDiseases,
                        onEvent,
                        shouldShowEditButton,
                        Modifier.padding(16.dp)
                    )
                else {
                    if (shouldShowCreateButton())
                        StartButton(
                            "No Past Medical History is present, create one"
                        ) {
                            onEvent(HistoryEvent.CreateButtonClicked)
                        }
                    else
                        CenteredText("No Past Medical History is present, a doctor can create one")
                }
            }

            is PmhMode.Edit -> PastMedicalHistoryFormContent(
                state.pastMedicalHistoryState.mode,
                onEvent,
                Modifier.padding(top = 8.dp)
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        TabSwitcher(
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            onTabSelected = {
                onEvent(HistoryEvent.TabSelected(it))
            },
            getTabTitle = {
                it.title
            },
            modifier = modifier.fillMaxWidth()
        )

        when (state.selectedTab) {
            HistoryScreenTab.PAST_MEDICAL_HISTORY -> {
                GetPastMedicalHistoryContent()
            }

            HistoryScreenTab.PAST_VISITS -> {
                VisitHistoryContent(state.pastVisitsState, onEvent)
            }
        }
    }
}