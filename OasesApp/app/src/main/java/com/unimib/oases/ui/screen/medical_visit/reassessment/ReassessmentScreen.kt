package com.unimib.oases.ui.screen.medical_visit.reassessment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
fun ReassessmentScreen(
    appViewModel: AppViewModel
) {
    val viewModel: ReassessmentViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    ReassessmentContent(state, viewModel::onEvent)
}

@Composable
fun ReassessmentContent(
    state: ReassessmentState,
    onEvent: (ReassessmentEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    val onCheckedChange: (Finding) -> Unit = {
        onEvent(
            ReassessmentEvent.FindingSelected(it)
        )
    }

    val isChecked: (Finding) -> Boolean = { it in state.findings }

    val onGenerateDefinitiveTherapiesPressed: () -> Unit = {
        onEvent(ReassessmentEvent.GenerateDefinitiveTherapiesClicked)
    }

    val onSubmit = { onEvent(ReassessmentEvent.SubmitClicked) }

    state.error?.let {
        Box(
            Modifier.fillMaxSize()
        ) {
            RetryButton(
                error = it,
                onClick = { onEvent(ReassessmentEvent.RetryButtonClicked) }
            )
        }
    } ?: Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        TitleText("Flag the findings present as diagnostic test results")

        Findings(state.possibleFindings, isChecked, onCheckedChange)

        GenerateDefinitiveTherapiesButton { onGenerateDefinitiveTherapiesPressed() }

        state.definitiveTherapies?.map { it.description }
            ?.let { DefinitiveTherapies(it) }

        SubmitButton(state.shouldShowSubmitButton) { onSubmit() }

        Spacer(Modifier.height(256.dp))
    }
}

@Composable
private fun GenerateDefinitiveTherapiesButton(
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onClick
        ) {
            Text("Suggest diagnosis and treatment", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SubmitButton(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    if (isVisible){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier.size(256.dp, 64.dp)
            ) {
                TitleText("Submit")
            }
        }
    }
}
