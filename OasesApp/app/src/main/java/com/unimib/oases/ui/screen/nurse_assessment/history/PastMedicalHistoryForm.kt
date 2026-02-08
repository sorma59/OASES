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
import com.unimib.oases.domain.model.DiseaseEntryType
import com.unimib.oases.domain.model.PmhGroup
import com.unimib.oases.ui.components.form.DateSelectorWithTodayButton
import com.unimib.oases.ui.components.input.LabeledSwitchButton
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.util.reactToKeyboardAppearance

@Composable
fun PastMedicalHistoryFormContent(
    state: PmhMode.Edit,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Box(Modifier.weight(1f)) {
            ChronicConditionsForm(
                state = state,
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
    state: PmhMode.Edit,
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
            val fontSize = 20.sp
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

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .reactToKeyboardAppearance()
        ) {

            LabeledSwitchButton(
                label = {
                    Text(
                        text = "No known chronic conditions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                checked = state.areAllSetToNo,
                onCheckedChange = { isChecked ->
                    onEvent(
                        if (isChecked)
                            HistoryEvent.DenyAllClicked
                        else
                            HistoryEvent.UndoMarkingAllAsNos
                    )
                },
                reversed = true
            )

//            LabeledRadioButton(
//                label = {
//                    Text(
//                        text = "No known chronic conditions",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                selected = state.areAllSetToNo,
//                onClick = { onEvent(HistoryEvent.DenyAllClicked) },
//                asReversed = true
//            )

            val diseases = state.editingDiseases

            diseases.forEachIndexed { index, diseaseState ->
                if (index == 0 || diseaseState.group != diseases[index - 1].group)
                    TitleText(diseaseState.group.displayName)
                when (diseaseState.entryType) {
                    DiseaseEntryType.SELECTION -> RadioButtonsInputWithDateAndText(
                        label = diseaseState.disease,
                        isDiagnosed = diseaseState.isDiagnosed,
                        onSelected = { isDiagnosed ->
                            onEvent(
                                HistoryEvent.RadioButtonClicked(
                                    diseaseState.disease,
                                    isDiagnosed
                                )
                            )
                        },
                        date = diseaseState.date,
                        onDateChange = {
                            onEvent(
                                HistoryEvent.DateChanged(
                                    diseaseState.disease,
                                    it
                                )
                            )
                        },
                        additionalInfo = diseaseState.additionalInfo,
                        onAdditionalInfoChange = {
                            onEvent(
                                HistoryEvent.AdditionalInfoChanged(
                                    diseaseState.disease,
                                    it
                                )
                            )
                        }
                    )
                    DiseaseEntryType.FREE_TEXT -> AnimatedLabelOutlinedTextField(
                        value = diseaseState.freeTextValue,
                        onValueChange = {
                            onEvent(
                                HistoryEvent.FreeTextChanged(
                                    disease = diseaseState.disease,
                                    text = it
                                )
                            )
                        },
                        labelText = "Details",
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp)) // Adds breathing room before bottom buttons
        }
    }
}

@Preview
@Composable
fun PastMedicalHistoryFormPreview() {
    PastMedicalHistoryFormContent(
        PmhMode.Edit(listOf(
            PatientDiseaseState(
                disease = "Disease 1",
                entryType = DiseaseEntryType.SELECTION,
                group = PmhGroup.ALLERGIES,
                isDiagnosed = true
            ),
            PatientDiseaseState(
                disease = "Disease 2",
                entryType = DiseaseEntryType.SELECTION,
                group = PmhGroup.ALLERGIES,
                isDiagnosed = true
            ),
            PatientDiseaseState(
                disease = "Disease 3",
                entryType = DiseaseEntryType.SELECTION,
                group = PmhGroup.NEUROPSYCHIATRIC,
                isDiagnosed = true
            ),
            PatientDiseaseState(
                disease = "Disease 4",
                entryType = DiseaseEntryType.FREE_TEXT,
                group = PmhGroup.VACCINATIONS,
                isDiagnosed = true
            ))
        ),
        onEvent = {}
    )
}