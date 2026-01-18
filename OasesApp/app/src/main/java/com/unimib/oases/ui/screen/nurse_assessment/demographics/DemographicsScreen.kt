package com.unimib.oases.ui.screen.nurse_assessment.demographics

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
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.form.AgeInputField
import com.unimib.oases.ui.components.form.DateSelectorWithTodayButton
import com.unimib.oases.ui.components.util.AnimatedLabelOutlinedTextField
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.OutlinedDropdown
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.util.reactToKeyboardAppearance

@Composable
fun DemographicsScreen(appViewModel: AppViewModel){

    val viewModel: DemographicsViewModel = hiltViewModel()
    
    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    LaunchedEffect(Unit) {
        viewModel.snackbarEvents.collect {
            appViewModel.showSnackbar(it)
        }
    }

    LoadingOverlay(state.isLoading)

    DemographicsContent(state, viewModel::onEvent)
}

@Composable
fun DemographicsContent(
    state: DemographicsState,
    onEvent: (DemographicsEvent) -> Unit
) {

    state.error?.let {
        RetryButton(
            error = it,
            onClick = { onEvent(DemographicsEvent.Retry) }
        )
    } ?: if (state.uiMode is PatientRegistrationScreensUiMode.Standalone && !state.uiMode.isEditing)
        DemographicsSummary(state.storedData, onEvent, Modifier.padding(16.dp))
    else
        DemographicsEditing(state, onEvent)
}

@Composable
private fun DemographicsEditing(
    state: DemographicsState,
    onEvent: (DemographicsEvent) -> Unit
) {

    val onNameChange = remember<(String) -> Unit> {
        { newName -> onEvent(DemographicsEvent.NameChanged(newName)) }
    }
    val onBirthDateChange = remember<(String) -> Unit> {
        { newDate -> onEvent(DemographicsEvent.BirthDateChanged(newDate)) }
    }
    val onAgeChange = remember<(Int) -> Unit> {
        { newAge -> onEvent(DemographicsEvent.AgeChanged(newAge)) }
    }
    val onSexChange = remember<(SexOption) -> Unit> {
        { newSex -> onEvent(DemographicsEvent.SexChanged(newSex)) }
    }
    val onVillageChange = remember<(String) -> Unit> {
        { newVillage -> onEvent(DemographicsEvent.VillageChanged(newVillage)) }
    }
    val onParishChange = remember<(String) -> Unit> {
        { newParish -> onEvent(DemographicsEvent.ParishChanged(newParish)) }
    }
    val onSubCountyChange = remember<(String) -> Unit> {
        { newSubCounty -> onEvent(DemographicsEvent.SubCountyChanged(newSubCounty)) }
    }
    val onDistrictChange = remember<(String) -> Unit> {
        { newDistrict -> onEvent(DemographicsEvent.DistrictChanged(newDistrict)) }
    }
    val onNextOfKinChange = remember<(String) -> Unit> {
        { newNextOfKin -> onEvent(DemographicsEvent.NextOfKinChanged(newNextOfKin)) }
    }
    val onContactChange = remember<(String) -> Unit> {
        { newContact -> onEvent(DemographicsEvent.ContactChanged(newContact)) }
    }

    val onDismiss = remember {
        { onEvent(DemographicsEvent.DismissDialog) }
    }

    val onConfirm = remember {
        { onEvent(DemographicsEvent.ConfirmDialog) }
    }

    val scrollState = rememberScrollState()

    @Composable
    fun BirthDateAndAgeInputFields(
        data: PatientData,
        errors: FormErrors
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateSelectorWithTodayButton(
                selectedDate = data.birthDate,
                onDateSelected = onBirthDateChange,
                labelText = "Date of Birth",
                isError = errors.birthDateError != null,
                modifier = Modifier.weight(1f)
            )

            Column {
                Icon(
                    Icons.Default.KeyboardDoubleArrowRight,
                    contentDescription = "Arrow to the right"
                )

                Icon(
                    Icons.Default.KeyboardDoubleArrowLeft,
                    contentDescription = "Arrow to the right"
                )
            }

            AgeInputField(
                ageInMonths = data.ageInMonths,
                onAgeChange = onAgeChange,
                isError = errors.birthDateError != null,
                modifier = Modifier.weight(1f)
            )
        }
    }

    state.editingState?.let {
        Column(
            Modifier.reactToKeyboardAppearance()
        ) {
            Box(
                Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .reactToKeyboardAppearance()
                        .padding(16.dp),

                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedLabelOutlinedTextField(
                        value = it.patientData.name,
                        onValueChange = onNameChange,
                        labelText = "Name",
                        isError = it.formErrors.nameError != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    it.formErrors.nameError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    BirthDateAndAgeInputFields(it.patientData, it.formErrors)

                    it.formErrors.birthDateError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    OutlinedDropdown(
                        selected = it.patientData.sexOption,
                        onSelected = onSexChange,
                        options = SexOption.entries,
                        optionToText = {sexOption: SexOption -> sexOption.displayName },
                        labelText = "Sex",
                        modifier = Modifier.fillMaxWidth(),
                        isError = it.formErrors.sexError != null
                    )

                    it.formErrors.sexError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                        

                    AnimatedLabelOutlinedTextField(
                        value = it.patientData.village,
                        onValueChange = onVillageChange,
                        labelText = "Village",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = it.patientData.parish,
                        onValueChange = onParishChange,
                        labelText = "Parish",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = it.patientData.subCounty,
                        onValueChange = onSubCountyChange,
                        labelText = "Sub-County",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = it.patientData.district,
                        onValueChange = onDistrictChange,
                        labelText = "District",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = it.patientData.nextOfKin,
                        onValueChange = onNextOfKinChange,
                        labelText = "Next-of-Kin",
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedLabelOutlinedTextField(
                        value = it.patientData.contact,
                        onValueChange = onContactChange,
                        labelText = "Contact",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                }
                FadeOverlay(modifier = Modifier.align(Alignment.BottomCenter))
            }

            BottomButtons(
                onCancel = { onEvent(DemographicsEvent.CancelButtonPressed) },
                onConfirm = { onEvent(DemographicsEvent.NextButtonPressed) }
            )
        }

        if (state.showAlertDialog) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = "Confirm patient saving")
                },
                text = { Text(text = "Do you want to save the patient to the database?") },
                confirmButton = {
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

enum class SexOption(val sex: Sex?, val displayName: String){
    MALE_OPTION(Sex.MALE, "Male"),
    FEMALE_OPTION(Sex.FEMALE, "Female"),
    UNSPECIFIED_OPTION(null, "Select sex");

    companion object {
        fun fromSex(sex: Sex): SexOption {
            return when (sex){
                Sex.MALE -> MALE_OPTION
                Sex.FEMALE -> FEMALE_OPTION
            }
        }
    }
}

enum class Sex(val displayName: String) {
    MALE("Male"),
    FEMALE("Female")
}