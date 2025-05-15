package com.unimib.oases.ui.components.util

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        onValueChange = {  },
        labelText = "Date",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { if (!readOnly)datePickerDialog.show() }) {
                Icon(Icons.Filled.CalendarMonth, contentDescription = "Insert a date")
            }
        }
    )
}