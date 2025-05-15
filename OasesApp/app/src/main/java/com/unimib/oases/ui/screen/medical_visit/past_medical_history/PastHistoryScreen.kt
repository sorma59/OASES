package com.unimib.oases.ui.screen.medical_visit.past_medical_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PastHistoryScreen() {

    val pastHistoryViewModel: PastHistoryViewModel = hiltViewModel()

    var showDialog by remember { mutableStateOf(false) }

    val chronicConditionsNames by remember { mutableStateOf(listOf<String>("Diabetes", "Hypertension", "Asthma", "Arthritis", "Depression")) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        ChronicConditionsCheckboxes(pastHistoryViewModel)

        Spacer(modifier = Modifier.height(16.dp))
        FloatingActionButton(
            onClick = { showDialog = true },
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
        ) {
            Icon(Icons.Filled.Add, "Add Chronic Disease")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CheckboxInput(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}

@Composable
fun ChronicConditionsCheckboxes(pastHistoryViewModel: PastHistoryViewModel){

    val chronicConditions by pastHistoryViewModel.chronicConditions.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {

        CheckboxInput(
            label = "Diabetes",
            checked = chronicConditions.diabetes,
            onCheckedChange = { pastHistoryViewModel.updateDiabetes(it) }
        )

        CheckboxInput(
            label = "Hypertension",
            checked = chronicConditions.hypertension,
            onCheckedChange = { pastHistoryViewModel.updateHyperTension(it) }
        )

        CheckboxInput(
            label = "Asthma",
            checked = chronicConditions.asthma,
            onCheckedChange = { pastHistoryViewModel.updateAsthma(it) }
        )

        CheckboxInput(
            label = "Arthritis",
            checked = chronicConditions.arthritis,
            onCheckedChange = { pastHistoryViewModel.updateArthritis(it) }
        )

        CheckboxInput(
            label = "Depression",
            checked = chronicConditions.depression,
            onCheckedChange = { pastHistoryViewModel.updateDepression(it) }
        )

        Spacer(modifier = Modifier.height(60.dp)) // Adds breathing room before bottom buttons
    }
}


