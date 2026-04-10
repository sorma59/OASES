package com.unimib.oases.ui.screen.medical_visit.evaluation.summary

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
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.ui.components.question.DetailsQuestions
import com.unimib.oases.ui.components.question.ImmediateTreatmentQuestions
import com.unimib.oases.ui.components.question.SupportiveTherapies
import com.unimib.oases.ui.components.question.Tests
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun EvaluationSummaryScreen(appViewModel: AppViewModel) {

    val viewModel: EvaluationSummaryViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    EvaluationSummaryContent(state, viewModel::onEvent)
}

@Composable
private fun EvaluationSummaryContent(
    state: EvaluationSummaryState,
    onEvent: (EvaluationSummaryEvent) -> Unit,
) {
    val scrollState = rememberScrollState()

    val isChecked: (LabelledTest) -> Boolean = { state.requestedTests.contains(it) }

    state.error?.let {
        Box(
            Modifier.fillMaxSize()
        ) {
            RetryButton(
                error = it,
                onClick = { onEvent(EvaluationSummaryEvent.RetryButtonClicked) }
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

            TitleText("Questions and answers:")

            ImmediateTreatmentQuestions(
                algorithms = state.immediateTreatmentAlgorithms,
                immediateTreatmentQuestions = state.immediateTreatmentQuestions,
                immediateTreatments = state.immediateTreatments,
                onNodeAnswer = { _, _, _ -> },
                readOnly = true,
            )

            DetailsQuestions(
                detailsQuestions = state.detailsQuestions,
                symptoms = state.symptoms,
                onSymptomSelected = { _, _ -> },
                readOnly = true,
            )

            Tests(
                state.conditions,
                isChecked,
                { },
                { state.additionalTestsText },
                { },
                readOnly = true,
            )

            SupportiveTherapies(state.supportiveTherapies)

            Spacer(Modifier.height(256.dp))
        }

        LargeFloatingActionButton(
            onClick = { onEvent(EvaluationSummaryEvent.EditButtonClicked) },
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit evaluation",
                modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
            )
        }
    }
}