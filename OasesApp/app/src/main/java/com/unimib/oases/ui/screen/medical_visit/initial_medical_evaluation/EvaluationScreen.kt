package com.unimib.oases.ui.screen.medical_visit.initial_medical_evaluation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.complaint.ComplaintQuestionWithImmediateTreatment
import com.unimib.oases.domain.model.complaint.LabelledTest
import com.unimib.oases.domain.model.complaint.MultipleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.SingleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.getLabels
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.input.LabeledRadioButton
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.medical_visit.initial_medical_evaluation.EvaluationEvent.SymptomSelected
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
    } ?: Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        TitleText("Ask these questions:")

        ImmediateTreatmentQuestions(state, onEvent)

        DetailsQuestions(
            state,
            onEvent
        )

        GenerateTestsButton(
            onGenerateTestsPressed,
            state.shouldShowGenerateTestsButton
        )

        state.complaint?.let {
            Tests(
                state,
                isChecked,
                onCheckedChange,
                additionalTestsText,
                onAdditionalTestsChanged
            )

            SupportiveTherapies(state)
        }

        SubmitButton(
            onSubmit,
            state.shouldShowSubmitButton
        )

        Spacer(Modifier.height(256.dp))
    }
}

@Composable
private fun SubmitButton(
    onClick: () -> Unit,
    visible: Boolean
) {
    if (visible) {
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
}

@Composable
private fun SupportiveTherapies(
    state: EvaluationState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        state.supportiveTherapies?.let { supportiveTherapies ->

            if (supportiveTherapies.isNotEmpty()){
                TitleText("Supportive therapies", fontSize = 18)
                HorizontalDivider(thickness = 0.8.dp)
                supportiveTherapies.forEach {
                    Text(it.text)
                    HorizontalDivider(thickness = 0.8.dp)
                }
            } else
                Text("No supportive therapies suggested")
        }
    }
}

@Composable
private fun GenerateTestsButton(
    onClick: () -> Unit,
    visible: Boolean
) {
    if (visible){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onClick
            ) {
                Text("Suggest tests and supportive therapies", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Tests(
    state: EvaluationState,
    isChecked: (LabelledTest) -> Boolean,
    onCheckedChange: (LabelledTest) -> Unit,
    additionalTestsText: () -> String,
    onAdditionalTestsChanged: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        if (state.conditions.isNotEmpty()){
            TitleText("Suggested tests (flag if you do order)")
            state.conditions.forEach { condition ->
                Column{
                    TitleText(condition.label, fontSize = 18)
                    Column(verticalArrangement =  Arrangement.spacedBy(8.dp)){
                        condition.suggestedTests.forEach { labelledTest ->
                            LabeledCheckbox(
                                label = labelledTest.label,
                                checked = isChecked(labelledTest),
                                onCheckedChange = { onCheckedChange(labelledTest) }
                            )
                        }
                    }
                }
            }
            AdditionalTests(additionalTestsText, onAdditionalTestsChanged)
        }
    }
}

@Composable
private fun AdditionalTests(
    text: () -> String,
    onValueChange: (String) -> Unit
) {
    Column{
        TitleText("Additional tests", fontSize = 18)

        OutlinedTextField(
            value = text(),
            onValueChange = onValueChange,
            label = { Text("Write here any additional test you have ordered") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailsQuestions(
    state: EvaluationState,
    onEvent: (EvaluationEvent) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.detailsQuestions.take(state.detailsQuestionsToShow).forEach { question ->
            when(question){
                is MultipleChoiceComplaintQuestion -> {
                    MultipleChoiceQuestion(
                        question = question,
                        isChecked = {
                            state.symptoms.contains(it)
                        },
                        onCheckedChange = { onEvent(SymptomSelected(it, question)) }
                    )
                }

                is SingleChoiceComplaintQuestion -> {
                    SingleChoiceQuestion(
                        question = question,
                        isSelected = {
                            state.symptoms.contains(it)
                        },
                        onSelected = { onEvent(SymptomSelected(it, question)) }
                    )
                }
            }
            if (question is ComplaintQuestionWithImmediateTreatment)
                if (question.shouldShowTreatment(state.symptoms)) {
                    TitleText("Immediate Treatment")
                    Text(question.treatment.text)
                }
        }
    }
}

@Composable
private fun ImmediateTreatmentQuestions(
    state: EvaluationState,
    onEvent: (EvaluationEvent) -> Unit,
    readOnly: Boolean = false
) {
    state.complaint?.let {

        val algorithms = state.immediateTreatmentAlgorithms.take(
            state.immediateTreatmentAlgorithmsToShow
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )  {
            algorithms.forEachIndexed { index, algorithm ->
                if (index <= state.immediateTreatmentQuestions.size) // Somewhat defensive
                    Column{
                        state.immediateTreatmentQuestions.elementAt(index).forEach { (node, answer) ->
                            YesOrNoQuestion(
                                question = node.value,
                                onAnswer = {
                                    onEvent(EvaluationEvent.NodeAnswered(it, node, algorithm))
                                },
                                enabled = !readOnly,
                                answer = answer
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        if (state.immediateTreatments.isNotEmpty()){
                            state.immediateTreatments.elementAt(index)?.let {
                                TitleText("Immediate Treatment")
                                Text(it.text)
                                Spacer(Modifier.height(16.dp))
                                HorizontalDivider(thickness = 0.8.dp)
                            }
                        }
                    }
            }
        }
    }
}

@Composable
private fun YesOrNoQuestion(
    question: String,
    onAnswer: (Boolean) -> Unit,
    enabled: Boolean,
    answer: Boolean?
){

    Column{
        Text(question)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Yes")

            RadioButton(
                selected = answer == true,
                onClick = {
                    if (enabled && answer != true) {
                        onAnswer(true)
                    }
                },
            )

            Spacer(Modifier.width(8.dp))

            Text("No")

            RadioButton(
                selected = answer == false,
                onClick = {
                    if (enabled && answer != false) {
                        onAnswer(false)
                    }
                },
            )
        }
    }
}

@Composable
private fun SingleChoiceQuestion(
    question: SingleChoiceComplaintQuestion,
    isSelected: (Symptom) -> Boolean,
    onSelected: (Symptom) -> Unit,
){

    val labels = question.getLabels()

    Column{
        TitleText(question.question)

        question.options.forEachIndexed { index, option ->
            LabeledRadioButton(
                label = { Text(text = labels[index]) },
                selected = isSelected(option),
                onClick = { onSelected(option) }
            )
        }
    }
}

@Composable
private fun MultipleChoiceQuestion(
    question: MultipleChoiceComplaintQuestion,
    isChecked: (Symptom) -> Boolean,
    onCheckedChange: (Symptom) -> Unit
){
    Column{
        TitleText(question.question)

        question.options.forEach { symptom ->
            LabeledCheckbox(
                label = symptom.label,
                checked = isChecked(symptom),
                onCheckedChange = { onCheckedChange(symptom) }
            )
        }
    }
}