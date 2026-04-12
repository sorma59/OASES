package com.unimib.oases.ui.screen.medical_visit.evaluation

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.ui.components.question.DetailsQuestions
import com.unimib.oases.ui.components.question.GenerateTestsButton
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
fun EvaluationScreen(
    appViewModel: AppViewModel
){

    val viewModel: EvaluationViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val additionalTestsText by viewModel.additionalTestsText.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    EvaluationContent(state, { additionalTestsText }, viewModel::onEvent)
}

@Composable
private fun EvaluationContent(
    state: EvaluationState,
    additionalTestsText: () -> String,
    onEvent: (EvaluationEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    val onAdditionalTestsChanged: (String) -> Unit = {
        onEvent(
            EvaluationEvent.AdditionalTestsTextChanged(it)
        )
    }

    val onCheckedChange: (LabelledTest) -> Unit = {
        onEvent(
            EvaluationEvent.TestSelected(it)
        )
    }

    val isChecked: (LabelledTest) -> Boolean = { state.requestedTests.contains(it) }

    val onGenerateTestsPressed: () -> Unit = {
        onEvent(EvaluationEvent.GenerateTestsPressed)
    }

    val onSubmit = { onEvent(EvaluationEvent.SubmitPressed) }

    state.error?.let {
        Box(
            Modifier.fillMaxSize()
        ) {
            RetryButton(
                error = it,
                onClick = { onEvent(EvaluationEvent.RetryButtonClicked) }
            )
        }
    } ?: if (state.isTriageMissing) {
        RetryButton(
            error = "Triage is needed",
            label = "Go to triage",
            onClick = { onEvent(EvaluationEvent.GoToTriageClicked) }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            TitleText("Ask these questions:")

            ImmediateTreatmentQuestions(
                algorithms = state.immediateTreatmentAlgorithms.take(
                    state.immediateTreatmentAlgorithmsToShow
                ),
                immediateTreatmentQuestions = state.immediateTreatmentQuestions,
                immediateTreatments = state.immediateTreatments,
                onNodeAnswer = { answer, node, tree ->
                    onEvent(EvaluationEvent.NodeAnswered(answer, node, tree))
                }
            )

            DetailsQuestions(
                detailsQuestions = state.detailsQuestions.take(state.detailsQuestionsToShow),
                symptoms = state.symptoms,
                onSymptomSelected = { symptom, question ->
                    onEvent(EvaluationEvent.SymptomSelected(symptom, question))
                },
            )

            GenerateTestsButton(
                onGenerateTestsPressed,
                state.shouldShowGenerateTestsButton
            )


            if (state.wereTestsGenerated){
                Tests(
                    state.conditions,
                    isChecked,
                    onCheckedChange,
                    additionalTestsText,
                    onAdditionalTestsChanged
                )

                SupportiveTherapies(state.supportiveTherapies?.map { it.therapy })

                SubmitButton(
                    onSubmit,
                )
            }

            Spacer(Modifier.height(256.dp))
        }
    }
}

@Composable
private fun SubmitButton(
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        Button(
            onClick = onClick,
            modifier = Modifier.size(256.dp, 64.dp)
        ) {
            TitleText("Submit")
        }
    }
}

