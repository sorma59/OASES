package com.unimib.oases.ui.screen.medical_visit.reassessment.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.complaint.Finding
import com.unimib.oases.ui.components.question.DefinitiveTherapies
import com.unimib.oases.ui.components.question.Findings
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun ReassessmentSummaryScreen(
    appViewModel: AppViewModel,
) {

    val viewModel: ReassessmentSummaryViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    ReassessmentSummaryContent(state, viewModel::onEvent)

}

@Composable
private fun ReassessmentSummaryContent(
    state: ReassessmentSummaryState,
    onEvent: (ReassessmentSummaryEvent) -> Unit,
) {

    val scrollState = rememberScrollState()

    val isChecked: (Finding) -> Boolean = { it in state.findings }

    state.error?.let {
        Box(
            Modifier.fillMaxSize()
        ) {
            RetryButton(
                error = it,
                onClick = { onEvent(ReassessmentSummaryEvent.RetryButtonClicked) }
            )
        }
    } ?: Box(
        Modifier.padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            TitleText("Tests, diagnosis and treatments")

            Findings(
                state.possibleFindings,
                isChecked,
                { },
                true,
            )

            DefinitiveTherapies(state.definitiveTherapies.toList())

            Spacer(Modifier.height(256.dp))
        }

        LargeFloatingActionButton(
            onClick = { onEvent(ReassessmentSummaryEvent.EditButtonClicked) },
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit reassessment",
                modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
            )
        }
    }

}

