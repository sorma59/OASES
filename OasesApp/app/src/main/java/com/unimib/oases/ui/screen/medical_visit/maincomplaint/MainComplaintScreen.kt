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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

@Composable
fun MainComplaintScreen(){

    val viewModel: MainComplaintViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val additionalTestsText by viewModel.additionalTestsText.collectAsState()

    val onAdditionalTestsChanged: (String) -> Unit = {
        viewModel.onEvent(
            MainComplaintEvent.AdditionalTestsTextChanged(it)
        )
    }

    val onCheckedChange: (Test) -> Unit = {
        viewModel.onEvent(
            MainComplaintEvent.TestSelected(it)
        )
    }

    val isChecked: (Test) -> Boolean = { state.requestedTests.contains(it) }

    val onGenerateTestsPressed: () -> Unit = {
        viewModel.onEvent(MainComplaintEvent.GenerateTestsPressed)
    }

    val shouldShowGenerateTestsButton = remember {
        derivedStateOf {
            state.detailsQuestions.isNotEmpty() &&
            state.detailsQuestionsToShow == state.detailsQuestions.size
        }
    }

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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){

            TitleText("Ask these questions:")

            ImmediateTreatmentQuestions(state, viewModel)

            DetailsQuestions(
                state,
                viewModel::onEvent
            )

            GenerateTestsButton(
                onGenerateTestsPressed,
                shouldShowGenerateTestsButton.value
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

            Spacer(Modifier.height(256.dp))
        }
}

@Composable
private fun SupportiveTherapies(
    state: MainComplaintState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        state.supportiveTherapies?.let {
            TitleText("Supportive therapies", fontSize = 18)

            if (it.isNotEmpty()){
                HorizontalDivider(thickness = 0.8.dp)
                for (supportiveTherapy in it) {
                    Text(supportiveTherapy.text)
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
            Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onClick
            ) {
                Text("Suggest tests and supportive therapies")
            }
        }
    }
}

@Composable
private fun Tests(
    state: MainComplaintState,
    isChecked: (Test) -> Boolean,
    onCheckedChange: (Test) -> Unit,
    additionalTestsText: String,
    onAdditionalTestsChanged: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        if (state.conditions.isNotEmpty()){
            TitleText("Suggested tests (flag if you do order)")
            for (condition in state.conditions) {
                Column{
                    TitleText(condition.label, fontSize = 18)
                    Column(verticalArrangement =  Arrangement.spacedBy(8.dp)){
                        for (test in condition.suggestedTests) {
                            LabeledCheckbox(
                                label = test.label,
                                checked = isChecked(test),
                                onCheckedChange = { onCheckedChange(test) }
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
    text: String,
    onValueChange: (String) -> Unit
) {
    Column{
        TitleText("Additional tests", fontSize = 18)

        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text("Write here any additional test you have ordered") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailsQuestions(
    state: MainComplaintState,
    onEvent: (MainComplaintEvent) -> Unit
) {

    Column {
        for (question in state.detailsQuestions.take(state.detailsQuestionsToShow)) {
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
        }
    }
}

@Composable
private fun ImmediateTreatmentQuestions(
    state: MainComplaintState,
    viewModel: MainComplaintViewModel,
    readOnly: Boolean = false
) {
    Column{
        for ((node, answer) in state.immediateTreatmentQuestions) {

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

        state.immediateTreatment?.let {
            TitleText("Immediate Treatment")
            Text(it.text)
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
private fun MultipleChoiceQuestion(
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