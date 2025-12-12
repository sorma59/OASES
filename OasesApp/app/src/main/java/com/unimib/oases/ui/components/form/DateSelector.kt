package com.unimib.oases.ui.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectorWithTodayButton(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    labelText: String = "Date",
    isError: Boolean = false,
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // Parse the initial date
    val initialMillis = remember(selectedDate) {
        try {
            if (selectedDate.isNotBlank()) {
                dateFormat.parse(selectedDate)?.time
            } else null
        } catch (_: Exception) {
            null
        }
    } ?: System.currentTimeMillis()

    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis() // no future dates
            }
        }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        TextButton(
                            onClick = {
                                val today = System.currentTimeMillis()
                                datePickerState.selectedDateMillis = today
                                datePickerState.displayedMonthMillis = today
                            }
                        ) {
                            Text("Today")
                        }
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                onDateSelected(dateFormat.format(Date(millis)))
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }

    AnimatedLabelOutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        labelText = labelText,
        modifier = modifier,
        readOnly = true,
        onClick = { if (!readOnly) showDialog = true },
        isError = isError,
    )
}