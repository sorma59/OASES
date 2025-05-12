package com.unimib.oases.ui.screen.patient_registration.info


import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
    name: String,
    onNameChanged: (String) -> Unit,
    age: String,
    onAgeChanged: (String) -> Unit,
    sex: String,
    onSexChanged: (String) -> Unit,
    village: String,
    onVillageChanged: (String) -> Unit,
    parish: String,
    onParishChanged: (String) -> Unit,
    subCountry: String,
    onSubCountryChanged: (String) -> Unit,
    district: String,
    onDistrictChanged: (String) -> Unit,
    nextOfKin: String,
    onNextOfKinChanged: (String) -> Unit,
    contact: String,
    onContactChanged: (String) -> Unit,
    date: String,
    onDateChanged: (String) -> Unit,
    time: String,
    onTimeChanged: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        AnimatedLabelOutlinedTextField(name, onNameChanged, "Name", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(age, onAgeChanged, "Age", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        SexDropdown(sex, onSexChanged, Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(village, onVillageChanged, "Village", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(parish, onParishChanged, "Parish", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(subCountry, onSubCountryChanged, "Sub Country", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(district, onDistrictChanged, "District", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(nextOfKin, onNextOfKinChanged, "Next of Kin", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedLabelOutlinedTextField(contact, onContactChanged, "Contact", Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        DateSelector(date, onDateChanged, Modifier.fillMaxWidth(), context)
        Spacer(modifier = Modifier.height(8.dp))

        CurrentTimeDisplay(onTimeChanged, Modifier.fillMaxWidth())
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
fun CurrentTimeDisplay(
    onTimeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var time by remember { mutableStateOf("") }
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("Europe/Rome") }

    LaunchedEffect(key1 = Unit) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"))
        time = timeFormat.format(calendar.time)
        onTimeChanged(time)
    }

    AnimatedLabelOutlinedTextField(
        value = time,
        onValueChange = { /* Non permettere la modifica diretta */ },
        labelText = "Time",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Filled.Timer, contentDescription = "Ora attuale")
        }
    )
}