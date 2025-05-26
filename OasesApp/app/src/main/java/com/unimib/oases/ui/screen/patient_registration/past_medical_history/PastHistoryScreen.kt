package com.unimib.oases.ui.screen.patient_registration.past_medical_history

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
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.DateSelector

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

    val state by pastHistoryViewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {

        for (disease in state.diseases){
            CheckboxInputWithDateAndText(
                label = disease.disease,
                checked = disease.isChecked,
                onCheckedChange = { pastHistoryViewModel.onEvent(PastHistoryEvent.CheckChanged(disease.disease)) },
                date = disease.date,
                onDateChange = { pastHistoryViewModel.onEvent(PastHistoryEvent.DateChanged(disease.disease, it)) },
                additionalInfo = disease.additionalInfo,
                onAdditionalInfoChange = { pastHistoryViewModel.onEvent(PastHistoryEvent.AdditionalInfoChanged(disease.disease, it)) }
            )
        }

        Spacer(modifier = Modifier.height(60.dp)) // Adds breathing room before bottom buttons
    }
}