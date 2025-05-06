package com.unimib.oases.ui.screen.patient_registration.info

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.theme.OasesTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun PatientInfoScreen() {

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var parish by remember { mutableStateOf("") }
    var subCountry by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var nextOfKin by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        AnimatedLabelOutlinedTextField(
            value = name,
            onValueChange = { name = it },
            labelText = "Name",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = age,
            onValueChange = { newText ->
                val filteredText = newText.filter { it.isDigit() }
                val newAge = filteredText.toIntOrNull() ?: 0
                if (newAge in 1..100 || filteredText.isEmpty()) {
                    age = filteredText
                }
            },
            labelText = "Age",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        SexDropdown(
            selectedSex = sex,
            onSexSelected = { sex = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = village,
            onValueChange = { village = it },
            labelText = "Village",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = parish,
            onValueChange = { parish = it },
            labelText = "Parish",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = subCountry,
            onValueChange = { subCountry = it },
            labelText = "Sub Country",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = district,
            onValueChange = { district = it },
            labelText = "District",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = nextOfKin,
            onValueChange = { nextOfKin = it },
            labelText = "Next of Kin",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            labelText = "Contact",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        DateSelector(
            selectedDate = date,
            onDateSelected = { date = it },
            modifier = Modifier.fillMaxWidth(),
            context = context
        )
        Spacer(modifier = Modifier.height(8.dp))

        TimeSelector(
            selectedTime = time,
            onTimeSelected = { time = it },
            modifier = Modifier.fillMaxWidth(),
            context = context
        )
    }
}

@Composable
fun AnimatedLabelOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SexDropdown(
    selectedSex: String,
    onSexSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val sexOptions = listOf("Maschio", "Femmina")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        AnimatedLabelOutlinedTextField(
            value = selectedSex,
            onValueChange = { /* Non permettere la modifica diretta */ },
            labelText = "Sesso",
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            anchorModifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sexOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onSexSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DateSelector(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    context: Context
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
            IconButton(onClick = { datePickerDialog.show() }) {
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
    context: Context
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minuteOfHour ->
            val formattedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour)
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
            IconButton(onClick = { timePickerDialog.show() }) {
                Icon(Icons.Filled.Timer, contentDescription = "Seleziona l'ora")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PatientInfoScreenPreview() {
    OasesTheme {
        PatientInfoScreen()
    }
}