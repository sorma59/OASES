package com.unimib.oases.ui.screen.medical_visit.disposition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.input.LabeledRadioButton
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun DispositionScreen(appViewModel: AppViewModel) {
    val viewModel: DispositionViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    DispositionContent(state, viewModel::onEvent, Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun DispositionContent(
    state: DispositionState,
    onEvent: (DispositionEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier.verticalScroll(
            rememberScrollState()
        ),
    ){

        Spacer(Modifier.height(32.dp))

        DispositionTypeQuestion(
            question = state.dispositionTypeQuestion,
            isSelected = { it.javaClass == state.dispositionType?.javaClass },
            onSelected = {
                onEvent(DispositionEvent.DispositionTypeSelected(it))
            }
        )

        WardQuestion(
            question = state.wardQuestion,
            isSelected = {
                state.dispositionType is DispositionType.Hospitalization
                && state.dispositionType.ward == it
            },
            onSelected = {
                onEvent(DispositionEvent.WardSelected(it))
            },
            isVisible = state.dispositionType is DispositionType.Hospitalization
        )

        state.suggestedHomeTreatments?.let {
            TitleText("Home Treatments")
            it.forEachIndexed { index, treatment ->
                Text(
                    text = treatment.label,
                    fontSize = 14.sp
                )

                if (index < it.size - 1){ HorizontalDivider() }
            }
        }

        if (state.areFreeTextsVisible) {
            AnimatedLabelOutlinedTextField(
                value = state.prescribedTreatmentsText,
                onValueChange = {
                    onEvent(DispositionEvent.PrescribedTreatmentsTextChanged(it))
                },
                labelText = "Write here any treatment you have prescribed",
            )

            AnimatedLabelOutlinedTextField(
                value = state.finalDiagnosisText,
                onValueChange = {
                    onEvent(DispositionEvent.FinalDiagnosisTextChanged(it))
                },
                labelText = "Write here your final diagnosis",
            )
        }

        SubmitButton(
            isVisible = state.isSubmitButtonVisible,
            onClick = {
                onEvent(DispositionEvent.CloseVisitClicked)
            }
        )

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun DispositionTypeQuestion(
    question: DispositionTypeQuestion,
    isSelected: (DispositionType) -> Boolean,
    onSelected: (DispositionType) -> Unit,
){

    Column{
        TitleText(question.question)

        question.options.forEach { option ->
            LabeledRadioButton(
                label = { Text(text = option.label) },
                selected = isSelected(option),
                onClick = { onSelected(option) }
            )
        }
    }
}

@Composable
private fun WardQuestion(
    question: WardQuestion,
    isSelected: (Ward) -> Boolean,
    onSelected: (Ward) -> Unit,
    isVisible: Boolean,
){

    if (isVisible){
        Column {
            TitleText(question.question)

            question.options.forEach { option ->
                LabeledRadioButton(
                    label = { Text(text = option.label) },
                    selected = isSelected(option),
                    onClick = { onSelected(option) }
                )
            }
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
                TitleText("Close visit")
            }
        }
    }
}

