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
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField

@Composable
fun VitalSignsScreen(
    sbp: String,
    dbp: String,
    spo2: String,
    hr: String,
    rr: String,
    temp: String,
    rbs: String,
    onSbpChanged: (String) -> Unit,
    onDbpChanged: (String) -> Unit,
    onSpo2Changed: (String) -> Unit,
    onHrChanged: (String) -> Unit,
    onRrChanged: (String) -> Unit,
    onTempChanged: (String) -> Unit,
    onRbsChanged: (String) -> Unit
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
            onValueChange = { if (isInteger(it)) onSbpChanged(it) },
            labelText = "Systolic Blood Pressure (SBP, mmHg)",
            modifier = Modifier.fillMaxWidth(),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = dbp,
            onValueChange = { if (isInteger(it)) onDbpChanged(it) },
            labelText = "Diastolic blood pressure (DBP, mmHg)",
            modifier = Modifier.fillMaxWidth(),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = spo2,
            onValueChange = { if (isInteger(it)) onSpo2Changed(it) },
            labelText = "Oxygen saturation (SpO2, %)",
            modifier = Modifier.fillMaxWidth(),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = hr,
            onValueChange = { if (isInteger(it)) onHrChanged(it) },
            labelText = "Heart rate (HR, bpm)",
            modifier = Modifier.fillMaxWidth(),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = rr,
            onValueChange = { if (isInteger(it)) onRrChanged(it) },
            labelText = "Respiratory rate (RR, bpm)",
            modifier = Modifier.fillMaxWidth(),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = temp,
            onValueChange = {
                if (isDecimal(it))
                    onTempChanged(it)
            },
            labelText = "Temperature (°C)",
            modifier = Modifier.fillMaxWidth(),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedLabelOutlinedTextField(
            value = rbs,
            onValueChange = {
                if (isDecimal(it))
                    onRbsChanged(it)
            },
            labelText = "Rapid blood sugar (RBS, mmol/L)",
            modifier = Modifier.fillMaxWidth(),
            isNumeric = true
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun isInteger(string: String):Boolean{
    return string.all{ch -> ch.isDigit()} || string.isEmpty()
}

fun isDecimal(string: String):Boolean{
    return string.matches(Regex("^\\d*\\.?\\d*$")  ) || string.isEmpty()
}

//Regex("^\\d*\\.?\\d* $ " )
//Regex("^\\d+\\.?\\d+$|^\\d+$")
