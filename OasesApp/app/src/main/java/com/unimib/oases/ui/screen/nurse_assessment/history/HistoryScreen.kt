package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.tab.TabSwitcher
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.ToastUtils

@Composable
fun HistoryScreen(
    appViewModel: AppViewModel
) {

    val viewModel: HistoryViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            ToastUtils.showToast(context, it)
        }
    }

    HistoryContent(state, viewModel::onEvent, Modifier.padding(16.dp))

}

@Composable
private fun HistoryContent(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
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
                if (state.pastMedicalHistoryState.isEditing)
                    PastMedicalHistoryFormContent(state.pastMedicalHistoryState, onEvent)
                else
                    PastHistorySummary(
                        state.pastMedicalHistoryState,
                        onEvent,
                        Modifier.padding(16.dp)
                    )
            }

            HistoryScreenTab.PAST_VISITS -> {
                VisitHistoryContent(state.pastVisitsState, onEvent)
            }
        }
    }
}