package com.unimib.oases.ui.screen.patient_registration.info

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.form.AgeInputField
import com.unimib.oases.ui.components.form.DateSelector
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.OutlinedDropdown
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

    var showAlertDialog by remember { mutableStateOf(false) }

    // Destructuring state
    val name = state.patient.name
    val birthDate = state.patient.birthDate
    val ageInMonths = state.patient.ageInMonths
    val sex = state.patient.sex
    val village = state.patient.village
    val parish = state.patient.parish
    val subCounty = state.patient.subCounty
    val district = state.patient.district
    val nextOfKin = state.patient.nextOfKin
    val contact = state.patient.contact
    val nameError = state.nameError
    val birthDateError = state.birthDateError
    val sexError = state.sexError
    val isLoading = state.isLoading
    val edited =state.edited

    // --- Remembered Lambdas --- to avoid recomposition,
    val onNameChange = remember<(String) -> Unit> {
        { newName -> patientInfoViewModel.onEvent(PatientInfoEvent.NameChanged(newName)) }
    }
    val onBirthDateChange = remember<(String) -> Unit> {
        { newDate -> patientInfoViewModel.onEvent(PatientInfoEvent.BirthDateChanged(newDate)) }
    }
    val onAgeChange = remember<(Int) -> Unit> {
        { newAge -> patientInfoViewModel.onEvent(PatientInfoEvent.AgeChanged(newAge)) }
    }
    val onSexChange = remember<(String) -> Unit> {
        { newSex -> patientInfoViewModel.onEvent(PatientInfoEvent.SexChanged(newSex)) }
    }
    val onVillageChange = remember<(String) -> Unit> {
        { newVillage -> patientInfoViewModel.onEvent(PatientInfoEvent.VillageChanged(newVillage)) }
    }
    val onParishChange = remember<(String) -> Unit> {
        { newParish -> patientInfoViewModel.onEvent(PatientInfoEvent.ParishChanged(newParish)) }
    }
    val onSubCountyChange = remember<(String) -> Unit> {
        { newSubCounty -> patientInfoViewModel.onEvent(PatientInfoEvent.SubCountyChanged(newSubCounty)) }
    }
    val onDistrictChange = remember<(String) -> Unit> {
        { newDistrict -> patientInfoViewModel.onEvent(PatientInfoEvent.DistrictChanged(newDistrict)) }
    }
    val onNextOfKinChange = remember<(String) -> Unit> {
        { newNextOfKin -> patientInfoViewModel.onEvent(PatientInfoEvent.NextOfKinChanged(newNextOfKin)) }
    }
    val onContactChange = remember<(String) -> Unit> {
        { newContact -> patientInfoViewModel.onEvent(PatientInfoEvent.ContactChanged(newContact)) }
    }

    val onValidateForm = remember {
        { patientInfoViewModel.onEvent(PatientInfoEvent.ValidateForm) }
    }
    val onConfirmSubmission = remember {
        {
            showAlertDialog = false
            patientInfoViewModel.onEvent(PatientInfoEvent.ConfirmSubmission)
        }
    }

    val context = LocalContext.current



    LaunchedEffect(key1 = context) {
        patientInfoViewModel.validationEvents.collect { event ->
            when (event) {
                is PatientInfoViewModel.ValidationEvent.ValidationSuccess -> {
                    showAlertDialog = true
                }

                is PatientInfoViewModel.ValidationEvent.SubmissionSuccess -> {
                    onSubmitted(state)
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    if (isLoading)
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
                        value = name,
                        onValueChange = onNameChange,
                        labelText = "Name",
                        isError = nameError != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (nameError != null)
                        Text(
                            text = nameError,
                            color = MaterialTheme.colorScheme.error
                        )

                    DateSelector(
                        selectedDate = birthDate,
                        onDateSelected = onBirthDateChange,
                        context = context,
                        labelText = "Date of Birth",
                        isError = birthDateError != null
                    )

                    if (birthDateError != null)
                        Text(
                            text = birthDateError,
                            color = MaterialTheme.colorScheme.error
                        )

                    AgeInputField(
                        ageInMonths = ageInMonths,
                        onAgeChange = onAgeChange
                    )

                    OutlinedDropdown(
                        selected = sex,
                        onSelected = onSexChange,
                        options = Sex.entries.map { it.displayName },
                        labelText = "Sex",
                        modifier = Modifier.fillMaxWidth(),
                        isError = sexError != null
                    )

                    if (sexError != null)
                        Text(
                            text = sexError,
                            color = MaterialTheme.colorScheme.error
                        )

                    AnimatedLabelOutlinedTextField(
                        value = village,
                        onValueChange = onVillageChange,
                        labelText = "Village",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = parish,
                        onValueChange = onParishChange,
                        labelText = "Parish",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = subCounty,
                        onValueChange = onSubCountyChange,
                        labelText = "Sub-County",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = district,
                        onValueChange = onDistrictChange,
                        labelText = "District",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = nextOfKin,
                        onValueChange = onNextOfKinChange,
                        labelText = "Next-of-Kin",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = contact,
                        onValueChange = onContactChange,
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
                    onClick = onValidateForm
                ) {
                    Text(if (edited) "Submit" else "Next")
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
                    onClick = onConfirmSubmission
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
    MALE("Male"),
    FEMALE("Female"),
    UNSPECIFIED("Unspecified");

    // Optional: Function to get enum from display name (useful for UI)
    companion object {
        fun fromDisplayName(displayName: String): Sex {
            return entries.find { it.displayName == displayName } ?: UNSPECIFIED
        }

        // Optional: Function to get enum from stored name (robust)
        fun fromStoredName(storedName: String): Sex? {
            return try {
                valueOf(storedName)
            } catch (e: IllegalArgumentException) {
                null // Handle cases where the stored name might be invalid
            }
        }
    }
}