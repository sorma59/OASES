package com.unimib.oases.ui.screen.medical_visit.info

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.theme.OasesTheme

@Composable
fun PatientInfoScreen(
    patientName: String = "",
    patientAge: String = "",
    patientSex: String = "",
    patientVillage: String = "",
    patientParish: String = "",
    patientSubCountry: String = "",
    patientDistrict: String = "",
    patientNextOfKin: String = "",
    patientContact: String = "",
    patientDate: String = "",
    patientTime: String = ""
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        AnimatedLabelOutlinedTextField(
            value = patientName,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Name",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientAge,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Age",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientSex,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Sex",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientVillage,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Village",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientParish,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Parish",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientSubCountry,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Sub Country",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientDistrict,
            onValueChange = { }, // Disabilita la modifica
            labelText = "District",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientNextOfKin,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Next of Kin",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientContact,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Contact",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientDate,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Date",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientTime,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Time",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
    }
}

@Composable
fun AnimatedLabelOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false // Aggiungiamo il parametro readOnly
) {
    var isFocused by remember { mutableStateOf(false) }
    val labelColor by animateColorAsState(
        targetValue = if (isFocused || value.isNotEmpty()) Color.Blue else Color.Gray,
        animationSpec = tween(durationMillis = 200),
        label = "labelColorAnimation"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText, color = labelColor) },
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        ),
        readOnly = readOnly // Utilizziamo il parametro readOnly
    )
}

@Preview(showBackground = true)
@Composable
fun PatientInfoScreenPreview() {
    OasesTheme {
        PatientInfoScreen(
            patientName = "Mario Rossi",
            patientAge = "35",
            patientSex = "Maschio",
            patientVillage = "Paese Bello",
            patientParish = "Parrocchia Centrale",
            patientSubCountry = "Sub-Regione A",
            patientDistrict = "Distretto 1",
            patientNextOfKin = "Luigi Bianchi",
            patientContact = "3331234567",
            patientDate = "05/05/2025",
            patientTime = "10:00"
        )
    }
}