package com.unimib.oases.ui.screen.medical_visit.maincomplaint

import androidx.compose.foundation.layout.Arrangement
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
import com.unimib.oases.ui.components.util.TitleText
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ){

        TitleText("Ask these questions:")

        QuestionsWithAnswers(state, viewModel)
    }
}

@Composable
private fun QuestionsWithAnswers(
    state: MainComplaintState,
    viewModel: MainComplaintViewModel,
    readOnly: Boolean = false
) {
    for ((node, answer) in state.questions) {

        Question(
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
        Spacer(Modifier.height(8.dp))
        Text(it.text)
    }
}

@Composable
fun Question(
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