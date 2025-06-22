package com.unimib.oases.ui.screen.patient_registration.info

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.DateSelector
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun PatientInfoScreen(
    onSubmitted: (PatientInfoState) -> Unit
) {

    val patientInfoViewModel: PatientInfoViewModel = hiltViewModel()

    val state by patientInfoViewModel.state.collectAsState()

    val context = LocalContext.current

    var showAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = context) {
        patientInfoViewModel.validationEvents.collect { event ->
            when (event) {
                is PatientInfoViewModel.ValidationEvent.ValidationSuccess -> {
                    showAlertDialog = true
                }

                is PatientInfoViewModel.ValidationEvent.SubmissionSuccess -> {
                    Log.d("PatientInfoScreen", "Final Submission Success")
                    onSubmitted(state)
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    if (state.isLoading)
        CustomCircularProgressIndicator()
    else {
        Column {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedLabelOutlinedTextField(
                        value = state.patient.name,
                        onValueChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.NameChanged(
                                    it
                                )
                            )
                        },
                        labelText = "Name",
                        isError = state.nameError != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (state.nameError != null)
                        Text(
                            text = state.nameError!!,
                            color = MaterialTheme.colorScheme.error
                        )

                    DateSelector(
                        selectedDate = state.patient.birthDate,
                        onDateSelected = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.BirthDateChanged(it)
                            )
                        },
                        context = context,
                        labelText = "Date of Birth"
                    )

                    AgeInputField(
                        ageInMonths = state.patient.ageInMonths,
                        onAgeChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.AgeChanged(
                                    if (it.toString().isEmpty()) -1 else it.toInt()
                                )
                            )
                        },
                        isError = state.ageError != null
                    )

                    if (state.ageError != null)
                        Text(
                            text = state.ageError!!,
                            color = MaterialTheme.colorScheme.error
                        )

                    SexDropdown(
                        selectedSex = state.patient.sex,
                        onSexSelected = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.SexChanged(
                                    it
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = state.patient.village,
                        onValueChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.VillageChanged(
                                    it
                                )
                            )
                        },
                        labelText = "Village",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = state.patient.parish,
                        onValueChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.ParishChanged(
                                    it
                                )
                            )
                        },
                        labelText = "Parish",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = state.patient.subCounty,
                        onValueChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.SubCountyChanged(
                                    it
                                )
                            )
                        },
                        labelText = "Sub Country",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = state.patient.district,
                        onValueChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.DistrictChanged(
                                    it
                                )
                            )
                        },
                        labelText = "District",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = state.patient.nextOfKin,
                        onValueChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.NextOfKinChanged(
                                    it
                                )
                            )
                        },
                        labelText = "Next of Kin",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = state.patient.contact,
                        onValueChange = {
                            patientInfoViewModel.onEvent(
                                PatientInfoEvent.ContactChanged(
                                    it
                                )
                            )
                        },
                        labelText = "Contact",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                }
                FadeOverlay(modifier = Modifier.align(Alignment.BottomCenter))
            }
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        patientInfoViewModel.onEvent(PatientInfoEvent.ValidateForm)
                    }
                ) {
                    Text("Submit")
                }
            }
        }
    }

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = {
                showAlertDialog = false
            },
            title = {
                Text(text = "Confirm patient saving")
            },
            text = {
                Text(text = "Do you want to save the patient to the database?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAlertDialog = false
                        patientInfoViewModel.onEvent(PatientInfoEvent.ConfirmSubmission)
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAlertDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SexDropdown(
    selectedSex: String,
    onSexSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val sexOptions = Sex.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        AnimatedLabelOutlinedTextField(
            value = selectedSex,
            onValueChange = { },
            labelText = "Sex",
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
                    text = { Text(text = option.displayName) },
                    onClick = {
                        onSexSelected(option.displayName)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AgeInputField(
    ageInMonths: Int?,
    onAgeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = true
) {
    var isMonths by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(ageInMonths, isMonths) {
        inputText = TextFieldValue(
            ageInMonths?.let {
                if (isMonths) it.toString() else (it / 12).toString()
            } ?: ""
        )
    }

    Box(modifier = modifier){

        OutlinedTextField(
            value = inputText,
            onValueChange = {
                val raw = it.text.toIntOrNull()
                if (raw != null && raw >= 0) {
                    onAgeChange(if (isMonths) raw else raw * 12)
                }
            },
            label = { Text(if (isMonths) "Age (months)" else "Age (years)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = isError
        )

        TextButton(
            onClick = {
                isMonths = !isMonths
                // Update the field with converted value
                val current = inputText.text.toIntOrNull() ?: 0
                val newValue = if (isMonths) current * 12 else current / 12
                inputText = TextFieldValue(newValue.toString())
                onAgeChange(if (isMonths) newValue else newValue * 12)
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(if (isMonths) "Switch to years" else "Infant?")
        }
    }
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
        onValueChange = { },
        labelText = "Time",
        modifier = modifier,
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Filled.Timer, contentDescription = "Current time")
        }
    )
}

enum class Sex(val displayName: String) {
    Male("Male"),
    Female("Female"),
    Unspecified("Unspecified")
}