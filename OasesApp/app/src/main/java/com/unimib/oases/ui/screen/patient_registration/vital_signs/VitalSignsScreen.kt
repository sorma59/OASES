package com.unimib.oases.ui.screen.patient_registration.vital_signs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.screen.medical_visit.info.AnimatedLabelOutlinedTextField

@Composable
fun VitalSignsScreen(
    sbp: String,
    dbp: String,
    spo2: String,
    hr: String,
    rr: String,
    temp: String,
    onSbpChanged: (String) -> Unit,
    onDbpChanged: (String) -> Unit,
    onSpo2Changed: (String) -> Unit,
    onHrChanged: (String) -> Unit,
    onRrChanged: (String) -> Unit,
    onTempChanged: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        AnimatedLabelOutlinedTextField(
            value = sbp,
            onValueChange = { if (it.isEmpty() || it.all { ch -> ch.isDigit() }) onSbpChanged(it) },
            labelText = "SBP (Systolic Blood Pressure)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = dbp,
            onValueChange = { if (it.isEmpty() || it.all { ch -> ch.isDigit() }) onDbpChanged(it) },
            labelText = "DBP (Diastolic Blood Pressure)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = spo2,
            onValueChange = { if (it.isEmpty() || it.all { ch -> ch.isDigit() }) onSpo2Changed(it) },
            labelText = "SpO₂ (Oxygen Saturation)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = hr,
            onValueChange = { if (it.isEmpty() || it.all { ch -> ch.isDigit() }) onHrChanged(it) },
            labelText = "HR (Heart Rate)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = rr,
            onValueChange = { if (it.isEmpty() || it.all { ch -> ch.isDigit() }) onRrChanged(it) },
            labelText = "RR (Respiratory Rate)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = temp,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$")))
                    onTempChanged(it)
            },
            labelText = "Temp (°C)",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
