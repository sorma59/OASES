package com.unimib.oases.ui.screen.medical_visit.pmh

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.form.DateSelectorWithTodayButton
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.ToastUtils
import com.unimib.oases.util.reactToKeyboardAppearance

@Composable
fun PastHistoryScreen(
    appViewModel: AppViewModel,
    readOnly: Boolean = false
) {

    val viewModel: PastHistoryViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            ToastUtils.showToast(context, message)
        }
        viewModel.onEvent(PastHistoryEvent.ToastShown)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    PastHistoryContent(state, viewModel::onEvent, readOnly)
}

@Composable
private fun PastHistoryContent(
    state: PastHistoryState,
    onEvent: (PastHistoryEvent) -> Unit,
    readOnly: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Box(Modifier.weight(1f)) {
            state.error?.let {
                RetryButton(
                    error = it,
                    onClick = { onEvent(PastHistoryEvent.Retry) }
                )
            } ?: if (state.isLoading) {
                CustomCircularProgressIndicator()
            } else {
                ChronicConditionsForm(
                    state = state,
                    onEvent = onEvent,
                    readOnly = readOnly
                )
            }
        }

        BottomButtons(
            onCancel = { onEvent(PastHistoryEvent.Cancel) },
            onConfirm = { onEvent(PastHistoryEvent.Save) }
        )
    }
}

@Composable
fun RadioButtonsInputWithDateAndText(
    label: String,
    isDiagnosed: Boolean?,
    onSelected: (Boolean) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    additionalInfo: String,
    onAdditionalInfoChange: (String) -> Unit,
    readOnly: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton( // "Yes" button
                selected = isDiagnosed == true,
                onClick = { if (!readOnly) onSelected(true)}
            )
            RadioButton( // "No"  button
                selected = isDiagnosed == false,
                onClick = { if (!readOnly) onSelected(false) }
            )
        }

        if (isDiagnosed == true){
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                DateSelectorWithTodayButton(
                    selectedDate = date,
                    onDateSelected = { onDateChange(it) },
                    readOnly = readOnly
                )

                AnimatedLabelOutlinedTextField(
                    value = additionalInfo,
                    onValueChange = { onAdditionalInfoChange(it) },
                    labelText = "Additional Info",
                    readOnly = readOnly
                )
            }
        }
    }
}

@Composable
fun ChronicConditionsForm(
    state: PastHistoryState,
    onEvent: (PastHistoryEvent) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
){

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ){
            Text(
                text = "Is the patient affected by the following disease?",
                modifier = Modifier.weight(1f)
            )

            Text("  Yes    No  ")
        }

        HorizontalDivider()

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .reactToKeyboardAppearance()
        ) {
            for (disease in state.diseases) {
                RadioButtonsInputWithDateAndText(
                    label = disease.disease,
                    isDiagnosed = disease.isDiagnosed,
                    onSelected = { isDiagnosed ->
                        onEvent(
                            PastHistoryEvent.RadioButtonClicked(
                                disease.disease,
                                isDiagnosed
                            )
                        )
                    },
                    date = disease.date,
                    onDateChange = {
                        onEvent(
                            PastHistoryEvent.DateChanged(
                                disease.disease,
                                it
                            )
                        )
                    },
                    additionalInfo = disease.additionalInfo,
                    onAdditionalInfoChange = {
                        onEvent(
                            PastHistoryEvent.AdditionalInfoChanged(
                                disease.disease,
                                it
                            )
                        )
                    },
                    readOnly = readOnly
                )
            }

            Spacer(modifier = Modifier.height(60.dp)) // Adds breathing room before bottom buttons
        }
    }
}

@Preview
@Composable
fun PastHistoryPreview() {
    PastHistoryContent(
        PastHistoryState(
            patientId = "",
            diseases = listOf(
                PatientDiseaseState(
                    disease = "Disease 1",
                    isDiagnosed = true
                ),
                PatientDiseaseState(
                    disease = "Disease 2",
                    isDiagnosed = true
                ),
                PatientDiseaseState(
                    disease = "Disease 3",
                    isDiagnosed = true
                ),
                PatientDiseaseState(
                    disease = "Disease 4",
                    isDiagnosed = true
                )
            )
        ),
        onEvent = {},
        readOnly = false,
    )
}