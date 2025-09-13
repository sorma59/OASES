package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.unimib.oases.domain.model.complaint.AspectQuestion
import com.unimib.oases.domain.model.complaint.DurationQuestion
import com.unimib.oases.domain.model.complaint.FrequencyQuestion
import com.unimib.oases.domain.model.complaint.MultipleChoiceComplaintQuestion
import com.unimib.oases.domain.model.complaint.Option
import com.unimib.oases.domain.model.complaint.SingleChoiceComplaintQuestion
import com.unimib.oases.domain.model.symptom.Symptom
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.input.LabeledRadioButton
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintEvent.AspectSelected
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintEvent.DurationSelected
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintEvent.FrequencySelected
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintEvent.SymptomSelected
import com.unimib.oases.ui.util.ToastUtils

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

                    val isSelected = when(question){
                        is DurationQuestion -> { option: Option ->
                            state.durationOption == option
                        }
                        is FrequencyQuestion -> { option: Option ->
                            state.frequencyOption == option
                        }

                        is AspectQuestion -> { option: Option ->
                            state.aspectOption == option
                        }
                    }

                    val onSelected = when(question){
                        is DurationQuestion -> { option: Option ->
                            onEvent(DurationSelected(option))
                        }
                        is FrequencyQuestion -> { option: Option ->
                            onEvent(FrequencySelected(option))
                        }

                        is AspectQuestion -> { option: Option ->
                            onEvent(AspectSelected(option))
                        }
                    }

                    SingleChoiceQuestion(
                        question = question,
                        isSelected = isSelected,
                        onSelected = onSelected
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
    isSelected: (Option) -> Boolean,
    onSelected: (Option) -> Unit,
){
    Column{
        TitleText(question.question)

        for (option in question.options) {
            LabeledRadioButton(
                label = option.displayText,
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