package com.unimib.oases.ui.screen.patient_registration.vital_signs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.screen.medical_visit.info.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.theme.OasesTheme

@Composable
fun VitalSignsScreen() {
    var sbp by remember { mutableStateOf("") }
    var dbp by remember { mutableStateOf("") }
    var spo2 by remember { mutableStateOf("") }
    var hr by remember { mutableStateOf("") }
    var rr by remember { mutableStateOf("") }
    var temp by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedLabelOutlinedTextField(
            value = sbp,
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.all { it.isDigit() }) {
                    sbp = newText
                }
            },
            labelText = "SBP (Pressione Sistolica)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = dbp,
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.all { it.isDigit() }) {
                    dbp = newText
                }
            },
            labelText = "DBP (Pressione Diastolica)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = spo2,
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.all { it.isDigit() }) {
                    spo2 = newText
                }
            },
            labelText = "SpO2 (%)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = hr,
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.all { it.isDigit() }) {
                    hr = newText
                }
            },
            labelText = "HR (Frequenza Cardiaca)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = rr,
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.all { it.isDigit() }) {
                    rr = newText
                }
            },
            labelText = "RR (Frequenza Respiratoria)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = temp,
            onValueChange = { newText ->
                // Permetti cifre e un solo punto decimale
                if (newText.isEmpty() || newText.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    temp = newText
                }
            },
            labelText = "Temp (°C)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Puoi aggiungere qui un bottone per salvare o processare i dati
    }
}

@Preview(showBackground = true)
@Composable
fun VitalSignsScreenPreview() {
    OasesTheme {
        VitalSignsScreen()
    }
}