package com.unimib.oases.ui.screen.medical_visit.past_medical_history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.home_page.components.card.ChronicConditionCard
import com.unimib.oases.ui.home_page.components.card.ChronicConditionUi
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PastHistoryScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val chronicConditions = remember {
        mutableStateListOf<ChronicConditionUi>().apply {
            repeat(30) {
                add(ChronicConditionUi(name = "Malattia Cronica Esempio", diagnosisDate = "01/01/2023"))
            }
        }
    }
    var newConditionName by remember { mutableStateOf("") }
    var newDiagnosisDate by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chronicConditions) { condition ->
                    ChronicConditionCard(condition = condition)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FloatingActionButton(
                onClick = { showDialog = true },
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Filled.Add, "Aggiungi malattia cronica")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (showDialog) {
            AddChronicConditionDialog(
                onDismissRequest = { showDialog = false },
                onConditionAdded = { name, date ->
                    chronicConditions.add(ChronicConditionUi(name = name, diagnosisDate = date))
                    showDialog = false
                    newConditionName = ""
                    newDiagnosisDate = ""
                },
                conditionName = newConditionName,
                onConditionNameChanged = { newConditionName = it },
                diagnosisDate = newDiagnosisDate,
                onDiagnosisDateChanged = { newDiagnosisDate = it },
                context = context
            )
        }
    }
}

@Composable
fun AddChronicConditionDialog(
    onDismissRequest: () -> Unit,
    onConditionAdded: (String, String) -> Unit,
    conditionName: String,
    onConditionNameChanged: (String) -> Unit,
    diagnosisDate: String,
    onDiagnosisDateChanged: (String) -> Unit,
    context: Context
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Aggiungi Malattia Cronica") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AnimatedLabelOutlinedTextField(
                    value = conditionName,
                    onValueChange = onConditionNameChanged,
                    labelText = "Nome",
                    modifier = Modifier.fillMaxWidth()
                )
                DatePickerInput(
                    selectedDate = diagnosisDate,
                    onDateSelected = onDiagnosisDateChanged,
                    modifier = Modifier.fillMaxWidth(),
                    context = context
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConditionAdded(conditionName, diagnosisDate)
                },
                enabled = conditionName.isNotBlank() && diagnosisDate.isNotBlank()
            ) {
                Text("Aggiungi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Annulla")
            }
        }
    )
}

@Composable
fun DatePickerInput(
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
        onValueChange = {},
        labelText = "Data",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Filled.CalendarMonth, contentDescription = "Seleziona la data")
            }
        }
    )
}

@Composable
fun AnimatedLabelOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
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
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}

