package com.unimib.oases.ui.screen.medical_visit.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.form.DateSelector
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.TimeSelector
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

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
    onPatientDateChanged: (String) -> Unit = {},
    patientTime: String = "",
    onPatientTimeChanged: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val timeZoneRome = TimeZone.getTimeZone("Europe/Rome")
    val calendar = Calendar.getInstance(timeZoneRome)
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    timeFormatter.timeZone = timeZoneRome
    val currentDate = dateFormatter.format(calendar.time)
    val currentTime = timeFormatter.format(calendar.time)

    var selectedDate by remember { mutableStateOf(patientDate.ifEmpty { currentDate }) }
    var selectedTime by remember { mutableStateOf(patientTime.ifEmpty { currentTime }) }

    LaunchedEffect(key1 = Unit) {
        onPatientDateChanged(selectedDate)
        onPatientTimeChanged(selectedTime)
    }

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
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientAge,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Age",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientSex,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Sex",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientVillage,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Village",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientParish,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Parish",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientSubCountry,
            onValueChange = { }, // Disabilita la modifica
            labelText = "SubCounty",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientDistrict,
            onValueChange = { }, // Disabilita la modifica
            labelText = "District",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientNextOfKin,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Next of Kin",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = patientContact,
            onValueChange = { }, // Disabilita la modifica
            labelText = "Contact",
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        DateSelector(
            selectedDate = selectedDate,
            onDateSelected = {
                selectedDate = it
                onPatientDateChanged(it)
            },
            context = context,
            readOnly = true // Imposta a true per disabilitare la modifica diretta
        )
        Spacer(modifier = Modifier.height(8.dp))

        TimeSelector(
            selectedTime = selectedTime,
            onTimeSelected = {
                selectedTime = it
                onPatientTimeChanged(it)
            },
            context = context,
            readOnly = true // Imposta a true per disabilitare la modifica diretta
        )
    }
}
