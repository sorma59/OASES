package com.unimib.oases.ui.components.util

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun TimeSelector(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    context: Context,
    readOnly: Boolean = false
) {
    val timeZone = TimeZone.getTimeZone("Africa/Kampala")
    val calendar = Calendar.getInstance(timeZone)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minuteOfHour ->
            val calendarNew = Calendar.getInstance(timeZone)
            calendarNew.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarNew.set(Calendar.MINUTE, minuteOfHour)

            val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeFormatter.timeZone = timeZone
            val formattedTime = timeFormatter.format(calendarNew.time)
            onTimeSelected(formattedTime)
        },
        hour,
        minute,
        true // is24HourView
    )

    AnimatedLabelOutlinedTextField(
        value = selectedTime,
        onValueChange = { },
        labelText = "Time",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { if (!readOnly) timePickerDialog.show() }) {
                Icon(Icons.Filled.Timer, contentDescription = "Insert Time")
            }
        }
    )
}