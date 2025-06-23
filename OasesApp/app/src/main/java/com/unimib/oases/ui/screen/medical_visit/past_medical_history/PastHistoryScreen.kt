package com.unimib.oases.ui.screen.medical_visit.past_medical_history

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.form.DateSelector
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField

@Composable
fun PastHistoryScreen() {

    val pastHistoryViewModel: PastHistoryViewModel = hiltViewModel()

    Box(Modifier.fillMaxSize()) {
        ChronicConditionsCheckboxes(pastHistoryViewModel)
    }
}

@Composable
fun CheckboxInputWithDateAndText(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    additionalInfo: String,
    onAdditionalInfoChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label)
        }

        if (checked){
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                DateSelector(
                    selectedDate = date,
                    onDateSelected = { onDateChange(it) },
                    context = LocalContext.current
                )

                AnimatedLabelOutlinedTextField(
                    value = additionalInfo,
                    onValueChange = { onAdditionalInfoChange(it) },
                    labelText = "Additional Info",
                )
            }
        }
    }
}

@Composable
fun ChronicConditionsCheckboxes(pastHistoryViewModel: PastHistoryViewModel){

    val chronicConditions by pastHistoryViewModel.chronicConditions.collectAsState()
    val chronicConditionsDates by pastHistoryViewModel.chronicConditionsDates.collectAsState()
    val chronicConditionAdditionalInfo by pastHistoryViewModel.chronicConditionAdditionalInfo.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {

        CheckboxInputWithDateAndText(
            label = "Diabetes",
            checked = chronicConditions.diabetes,
            onCheckedChange = { pastHistoryViewModel.updateDiabetes(it) },
            date = chronicConditionsDates.diabetes,
            onDateChange = { pastHistoryViewModel.updateDiabetesDate(it) },
            additionalInfo = chronicConditionAdditionalInfo.diabetes,
            onAdditionalInfoChange = { pastHistoryViewModel.updateDiabetesInfo(it) }
        )

        CheckboxInputWithDateAndText(
            label = "Hypertension",
            checked = chronicConditions.hypertension,
            onCheckedChange = { pastHistoryViewModel.updateHyperTension(it) },
            date = chronicConditionsDates.hypertension,
            onDateChange = { pastHistoryViewModel.updateHyperTensionDate(it) },
            additionalInfo = chronicConditionAdditionalInfo.hypertension,
            onAdditionalInfoChange = { pastHistoryViewModel.updateHyperTensionInfo(it) }
        )

        CheckboxInputWithDateAndText(
            label = "Asthma",
            checked = chronicConditions.asthma,
            onCheckedChange = { pastHistoryViewModel.updateAsthma(it) },
            date = chronicConditionsDates.asthma,
            onDateChange = { pastHistoryViewModel.updateAsthmaDate(it) },
            additionalInfo = chronicConditionAdditionalInfo.asthma,
            onAdditionalInfoChange = { pastHistoryViewModel.updateAsthmaInfo(it) }
        )

        CheckboxInputWithDateAndText(
            label = "Arthritis",
            checked = chronicConditions.arthritis,
            onCheckedChange = { pastHistoryViewModel.updateArthritis(it) },
            date = chronicConditionsDates.arthritis,
            onDateChange = { pastHistoryViewModel.updateArthritisDate(it) },
            additionalInfo = chronicConditionAdditionalInfo.arthritis,
            onAdditionalInfoChange = { pastHistoryViewModel.updateArthritisInfo(it) }
        )

        CheckboxInputWithDateAndText(
            label = "Depression",
            checked = chronicConditions.depression,
            onCheckedChange = { pastHistoryViewModel.updateDepression(it) },
            date = chronicConditionsDates.depression,
            onDateChange = { pastHistoryViewModel.updateDepressionDate(it) },
            additionalInfo = chronicConditionAdditionalInfo.depression,
            onAdditionalInfoChange = { pastHistoryViewModel.updateDepressionInfo(it) }
        )

        Spacer(modifier = Modifier.height(60.dp)) // Adds breathing room before bottom buttons
    }
}


