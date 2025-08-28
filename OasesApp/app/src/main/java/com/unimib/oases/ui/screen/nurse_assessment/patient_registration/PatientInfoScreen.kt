package com.unimib.oases.ui.screen.nurse_assessment.patient_registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.form.AgeInputField
import com.unimib.oases.ui.components.form.DateSelector
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.OutlinedDropdown
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.ValidationEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun PatientInfoScreen(
    state: PatientInfoState,
    onEvent: (PatientInfoEvent) -> Unit,
    validationEvents: Flow<ValidationEvent>
) {

//    val patientInfoViewModel: PatientInfoViewModel = hiltViewModel()

//    val state by patientInfoViewModel.state.collectAsState()

    var showAlertDialog by remember { mutableStateOf(false) }

    // --- Remembered Lambdas --- to avoid recomposition,
    val onNameChange = remember<(String) -> Unit> {
        { newName -> onEvent(PatientInfoEvent.NameChanged(newName)) }
    }
    val onBirthDateChange = remember<(String) -> Unit> {
        { newDate -> onEvent(PatientInfoEvent.BirthDateChanged(newDate)) }
    }
    val onAgeChange = remember<(Int) -> Unit> {
        { newAge -> onEvent(PatientInfoEvent.AgeChanged(newAge)) }
    }
    val onSexChange = remember<(String) -> Unit> {
        { newSex -> onEvent(PatientInfoEvent.SexChanged(newSex)) }
    }
    val onVillageChange = remember<(String) -> Unit> {
        { newVillage -> onEvent(PatientInfoEvent.VillageChanged(newVillage)) }
    }
    val onParishChange = remember<(String) -> Unit> {
        { newParish -> onEvent(PatientInfoEvent.ParishChanged(newParish)) }
    }
    val onSubCountyChange = remember<(String) -> Unit> {
        { newSubCounty -> onEvent(PatientInfoEvent.SubCountyChanged(newSubCounty)) }
    }
    val onDistrictChange = remember<(String) -> Unit> {
        { newDistrict -> onEvent(PatientInfoEvent.DistrictChanged(newDistrict)) }
    }
    val onNextOfKinChange = remember<(String) -> Unit> {
        { newNextOfKin -> onEvent(PatientInfoEvent.NextOfKinChanged(newNextOfKin)) }
    }
    val onContactChange = remember<(String) -> Unit> {
        { newContact -> onEvent(PatientInfoEvent.ContactChanged(newContact)) }
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.ValidationSuccess -> {
                    if (state.isEdited)
                        showAlertDialog = true
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    if (state.isLoading)
        CustomCircularProgressIndicator()
    else if (state.error != null){
        RetryButton(
            error = state.error,
            onClick = { onEvent(PatientInfoEvent.Retry) }
        )
    }
    else {

        Box{
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedLabelOutlinedTextField(
                    value = state.patient.name,
                    onValueChange = onNameChange,
                    labelText = "Name",
                    isError = state.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.nameError != null)
                    Text(
                        text = state.nameError,
                        color = MaterialTheme.colorScheme.error
                    )

                DateSelector(
                    selectedDate = state.patient.birthDate,
                    onDateSelected = onBirthDateChange,
                    context = context,
                    labelText = "Date of Birth",
                    isError = state.birthDateError != null
                )

                if (state.birthDateError != null)
                    Text(
                        text = state.birthDateError,
                        color = MaterialTheme.colorScheme.error
                    )

                AgeInputField(
                    ageInMonths = state.patient.ageInMonths,
                    onAgeChange = onAgeChange
                )

                OutlinedDropdown(
                    selected = state.patient.sex,
                    onSelected = onSexChange,
                    options = Sex.entries.map { it.displayName },
                    labelText = "Sex",
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.sexError != null
                )

                if (state.sexError != null)
                    Text(
                        text = state.sexError,
                        color = MaterialTheme.colorScheme.error
                    )

                AnimatedLabelOutlinedTextField(
                    value = state.patient.village,
                    onValueChange = onVillageChange,
                    labelText = "Village",
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedLabelOutlinedTextField(
                    value = state.patient.parish,
                    onValueChange = onParishChange,
                    labelText = "Parish",
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedLabelOutlinedTextField(
                    value = state.patient.subCounty,
                    onValueChange = onSubCountyChange,
                    labelText = "Sub-County",
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedLabelOutlinedTextField(
                    value = state.patient.district,
                    onValueChange = onDistrictChange,
                    labelText = "District",
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedLabelOutlinedTextField(
                    value = state.patient.nextOfKin,
                    onValueChange = onNextOfKinChange,
                    labelText = "Next-of-Kin",
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedLabelOutlinedTextField(
                    value = state.patient.contact,
                    onValueChange = onContactChange,
                    labelText = "Contact",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
            FadeOverlay(modifier = Modifier.align(Alignment.BottomCenter))
        }
//            Row(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.End
//            ) {
//                Button(
//                    onClick = onNextButtonPressed
//                ) {
//                    Text(if (state.isEdited) "Submit" else "Next")
//                }
//            }

    }

//    if (showAlertDialog) {
//        AlertDialog(
//            onDismissRequest = {
//                showAlertDialog = false
//            },
//            title = {
//                Text(text = "Confirm patient saving")
//            },
//            text = {
//                Text(text = "Do you want to save the patient to the database?")
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = onConfirmSubmission
//                ) {
//                    Text("Confirm")
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        showAlertDialog = false
//                    }
//                ) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
}

enum class Sex(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    UNSPECIFIED("Unspecified");

    companion object {
        // Function to get enum from display name (useful for UI)
        fun fromDisplayName(displayName: String): Sex {
            return entries.find { it.displayName == displayName } ?: UNSPECIFIED
        }
    }
}