package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.complaint.MultipleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.SingleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.Test
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.input.LabeledRadioButton
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintEvent.SymptomSelected
import com.unimib.oases.ui.util.ToastUtils
import com.unimib.oases.util.toggle

@Composable
fun MainComplaintScreen(){

    val viewModel: MainComplaintViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    LaunchedEffect(state.toastMessage){
        state.toastMessage?.let {
            ToastUtils.showToast(context, it)
        }
        viewModel.onEvent(MainComplaintEvent.ToastShown)
    }
    
    state.error?.let{
        Box(
            Modifier
                .fillMaxSize()
        ){
            RetryButton(
                error = it,
                onClick = { viewModel.onEvent(MainComplaintEvent.RetryButtonClicked) }
            )
        }
    } ?:

    if (state.isLoading)
        CustomCircularProgressIndicator()
    else
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){

            TitleText("Ask these questions:")

            TreatmentPlanQuestions(state, viewModel)

            DetailsQuestions(
                state,
                viewModel::onEvent
            )

            GenerateTestsButton(viewModel)

            state.complaint?.let {
                Tests(
                    state,
                    isChecked = { state.requestedTests.contains(it) },
                    onCheckedChange = { state.requestedTests.toggle(it) }
                )
            }
        }
}

@Composable
private fun GenerateTestsButton(
    viewModel: MainComplaintViewModel
) {
    Box(
        Modifier.fillMaxWidth()
    ){
        Button(
            onClick = { viewModel.onEvent(MainComplaintEvent.GenerateTestsPressed) },
        ) {
            Text("Suggest tests")
        }
    }
}

@Composable
fun Tests(
    state: MainComplaintState,
    isChecked: (Test) -> Boolean,
    onCheckedChange: (Test) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        for (condition in state.conditions) {
            TitleText(condition.label, fontSize = 18)
            for (test in condition.suggestedTests){
                LabeledCheckbox(
                    label = test.label,
                    checked = isChecked(test),
                    onCheckedChange = { onCheckedChange(test) }
                )
            }
        }
    }
}

@Composable
private fun DetailsQuestions(
    state: MainComplaintState,
    onEvent: (MainComplaintEvent) -> Unit
) {

    Column {
        for (question in state.detailsQuestions) {
            when(question){
                is MultipleChoiceComplaintQuestion -> {
                    MultipleChoiceQuestion(
                        question = question,
                        isChecked = {
                            state.symptoms.contains(it)
                        },
                        onCheckedChange = { onEvent(SymptomSelected(it)) }
                    )
                }

                is SingleChoiceComplaintQuestion -> {
                    SingleChoiceQuestion(
                        question = question,
                        isSelected = {
                            state.symptoms.contains(it)
                        },
                        onSelected = { onEvent(SymptomSelected(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TreatmentPlanQuestions(
    state: MainComplaintState,
    viewModel: MainComplaintViewModel,
    readOnly: Boolean = false
) {
    for ((node, answer) in state.treatmentPlanQuestions) {

        YesOrNoQuestion(
            question = node.value,
            onAnswer = {
                viewModel.onEvent(MainComplaintEvent.NodeAnswered(it, node))
            },
            enabled = !readOnly,
            answer = answer
        )
    }

    Spacer(Modifier.height(8.dp))

    state.treatmentPlan?.let {
        TitleText("Treatment Plan")
        Text(it.text)
    }
}

@Composable
fun YesOrNoQuestion(
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
fun SingleChoiceQuestion(
    question: SingleChoiceComplaintQuestion,
    isSelected: (Symptom) -> Boolean,
    onSelected: (Symptom) -> Unit,
){
    Column{
        TitleText(question.question)

        for (option in question.options) {
            LabeledRadioButton(
                label = option.label,
                selected = isSelected(option),
                onClick = { onSelected(option) }
            )
        }
    }
}

@Composable
fun MultipleChoiceQuestion(
    question: MultipleChoiceComplaintQuestion,
    isChecked: (Symptom) -> Boolean,
    onCheckedChange: (Symptom) -> Unit
){
    Column{
        TitleText(question.question)

        for (symptom in question.options) {
            LabeledCheckbox(
                label = symptom.label,
                checked = isChecked(symptom),
                onCheckedChange = { onCheckedChange(symptom) }
            )
        }
    }
}