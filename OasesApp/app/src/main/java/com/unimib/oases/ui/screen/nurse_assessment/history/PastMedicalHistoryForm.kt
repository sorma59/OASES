package com.unimib.oases.ui.screen.nurse_assessment.history

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.ui.components.form.DateSelectorWithTodayButton
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.util.reactToKeyboardAppearance

@Composable
fun PastMedicalHistoryFormContent(
    diseases: List<PatientDiseaseState>,
    onEvent: (HistoryEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Box(Modifier.weight(1f)) {
            ChronicConditionsForm(
                diseases = diseases,
                onEvent = onEvent
            )
        }

        BottomButtons(
            onCancel = { onEvent(HistoryEvent.Cancel) },
            onConfirm = { onEvent(HistoryEvent.Save) }
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
    diseases: List<PatientDiseaseState>,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
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
            val fontSize = 18.sp
            val fontWeight = FontWeight.ExtraBold
            Text(
                text = "Is the patient affected by the following disease?",
                fontWeight = fontWeight,
                fontSize = fontSize,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "  Yes    No  ",
                fontWeight = fontWeight,
                fontSize = fontSize
            )
        }

        HorizontalDivider()

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .reactToKeyboardAppearance()
        ) {
            for (disease in diseases) {
                RadioButtonsInputWithDateAndText(
                    label = disease.disease,
                    isDiagnosed = disease.isDiagnosed,
                    onSelected = { isDiagnosed ->
                        onEvent(
                            HistoryEvent.RadioButtonClicked(
                                disease.disease,
                                isDiagnosed
                            )
                        )
                    },
                    date = disease.date,
                    onDateChange = {
                        onEvent(
                            HistoryEvent.DateChanged(
                                disease.disease,
                                it
                            )
                        )
                    },
                    additionalInfo = disease.additionalInfo,
                    onAdditionalInfoChange = {
                        onEvent(
                            HistoryEvent.AdditionalInfoChanged(
                                disease.disease,
                                it
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(60.dp)) // Adds breathing room before bottom buttons
        }
    }
}

@Preview
@Composable
fun PastMedicalHistoryFormPreview() {
    PastMedicalHistoryFormContent(
        listOf(
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
        ),
        onEvent = {}
    )
}