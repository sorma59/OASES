package com.unimib.oases.ui.screen.medical_visit.info

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    onPatientDateChanged: (String) -> Unit = {}, // Callback per la data
    patientTime: String = "",
    onPatientTimeChanged: (String) -> Unit = {} // Callback per l'ora
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

        DateSelector(
            selectedDate = selectedDate,
            onDateSelected = {
                selectedDate = it
                onPatientDateChanged(it)
            },
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
            context = context,
            readOnly = true // Imposta a true per disabilitare la modifica diretta
        )
    }
}

@Composable
fun AnimatedLabelOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    anchorModifier: Modifier = Modifier
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
        isError = isError,
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused }
            .then(anchorModifier),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        ),
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}

@Composable
fun DateSelector(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    context: Context,
    readOnly: Boolean = false
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, yearSelected, monthOfYear, dayOfMonth ->
            val calendarNew = Calendar.getInstance()
            calendarNew.set(yearSelected, monthOfYear, dayOfMonth)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendarNew.time)
            onDateSelected(formattedDate)
        },
        year,
        month,
        day
    )

    AnimatedLabelOutlinedTextField(
        value = selectedDate,
        onValueChange = { /* Non permettere la modifica diretta */ },
        labelText = "Date",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { if (!readOnly) datePickerDialog.show() }) {
                Icon(Icons.Filled.CalendarMonth, contentDescription = "Seleziona la data")
            }
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun TimeSelector(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    context: Context,
    readOnly: Boolean = false
) {
    val timeZoneRome = TimeZone.getTimeZone("Europe/Rome")
    val calendar = Calendar.getInstance(timeZoneRome)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minuteOfHour ->
            val calendarNew = Calendar.getInstance(timeZoneRome)
            calendarNew.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarNew.set(Calendar.MINUTE, minuteOfHour)

            val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeFormatter.timeZone = timeZoneRome
            val formattedTime = timeFormatter.format(calendarNew.time)
            onTimeSelected(formattedTime)
        },
        hour,
        minute,
        true // is24HourView
    )

    AnimatedLabelOutlinedTextField(
        value = selectedTime,
        onValueChange = { /* Non permettere la modifica diretta */ },
        labelText = "Time",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { if (!readOnly) timePickerDialog.show() }) {
                Icon(Icons.Filled.Timer, contentDescription = "Seleziona l'ora")
            }
        }
    )
}