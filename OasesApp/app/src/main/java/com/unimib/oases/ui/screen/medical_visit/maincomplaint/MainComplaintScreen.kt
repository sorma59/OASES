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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

        for((node, answer, callback) in state.questions){

            Question(
                question = node.value,
                onAnswer = {
                    viewModel.onEvent(MainComplaintEvent.NodeAnswered(it, node))
                    callback?.invoke(it)
                },
                onEditAttempt = {
                    viewModel.onEvent(MainComplaintEvent.NodeAnsweredAgain)
                },
                enabled = answer == null,
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
}

@Composable
fun Question(
    question: String,
    onAnswer: (Boolean) -> Unit,
    onEditAttempt: () -> Unit,
    enabled: Boolean,
    answer: Boolean?
){

    var answeredYes by remember { mutableStateOf<Boolean?>(answer) }

    Column{
        Text(question)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Yes")

            RadioButton(
                selected = answeredYes == true,
                onClick = {
                    if (enabled) {
                        answeredYes = true
                        onAnswer(true)
                    } else
                        onEditAttempt()
                },
            )

            Spacer(Modifier.width(8.dp))

            Text("No")

            RadioButton(
                selected = answeredYes == false,
                onClick = {
                    if (enabled) {
                        answeredYes = false
                        onAnswer(false)
                    } else
                        onEditAttempt()
                },
            )
        }
    }

}