package com.unimib.oases.ui.screen.nurse_assessment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.ValidationEvent
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningScreen
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoEvent
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoScreen
import com.unimib.oases.ui.screen.nurse_assessment.room_selection.RoomScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.ContinueToTriageDecisionScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.SubmissionScreen
import com.unimib.oases.ui.screen.nurse_assessment.triage.RedCodeScreen
import com.unimib.oases.ui.screen.nurse_assessment.triage.YellowCodeScreen
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsScreen
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun RegistrationScreen(
    appViewModel: AppViewModel
) {

    val registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel()

    val state by registrationScreenViewModel.state.collectAsState()

    val validationEvents = registrationScreenViewModel.validationEvents

    var showAlertDialog by remember { mutableStateOf(false) }

    val currentTab = state.currentTab

    val prefixText = remember(currentTab) {
        when (currentTab){
            Tab.RED_CODE, Tab.YELLOW_CODE -> {
                when (state.triageState.patientCategory){
                    PatientCategory.ADULT -> "Adult "
                    PatientCategory.PEDIATRIC -> "Pediatric "
                }
            }
            else -> ""
        }
    }

    val nextButtonText by remember(currentTab) { // Outer remember for tab changes
        derivedStateOf { // Inner derived state for isEdited/isNew changes within the DEMOGRAPHICS tab
            when (currentTab) {
                Tab.DEMOGRAPHICS -> {
                    if (state.patientInfoState.isEdited || state.patientInfoState.isNew)
                        "Submit"
                    else
                        "Next"
                }
                Tab.CONTINUE_TO_TRIAGE -> "Triage"
                Tab.VITAL_SIGNS, Tab.ROOM_SELECTION, Tab.RED_CODE, Tab.YELLOW_CODE, Tab.HISTORY, Tab.MALNUTRITION_SCREENING -> "Next"
                Tab.SUBMIT_ALL -> "Submit"
            }
        }
    }


    val cancelButtonText = remember(currentTab){
        when (currentTab) {
            Tab.DEMOGRAPHICS -> "Cancel"
            Tab.CONTINUE_TO_TRIAGE -> "Home"
            else -> "Back"
        }
    }

    val onConfirm by remember(currentTab, state.patientInfoState.isEdited, state.patientInfoState.isNew) {
        derivedStateOf {
            if (currentTab == Tab.DEMOGRAPHICS &&
                (state.patientInfoState.isEdited ||
                        state.patientInfoState.isNew)
            ) {
                { registrationScreenViewModel.onPatientInfoEvent(PatientInfoEvent.NextButtonPressed) }
            } else
                registrationScreenViewModel::onNext
        }
    }

    LaunchedEffect(Unit) {
        registrationScreenViewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.ValidationSuccess -> {
                    if (state.patientInfoState.isEdited)
                        showAlertDialog = true
                    else
                        registrationScreenViewModel.onNext()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = prefixText + currentTab.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (currentTab) {
                    Tab.DEMOGRAPHICS -> PatientInfoScreen(
                        state = state.patientInfoState,
                        onEvent = registrationScreenViewModel::onPatientInfoEvent,
                        validationEvents = registrationScreenViewModel.validationEvents
                    )
                    Tab.CONTINUE_TO_TRIAGE -> ContinueToTriageDecisionScreen()
                    Tab.VITAL_SIGNS -> VitalSignsScreen(
                        state = state.vitalSignsState,
                        onEvent = registrationScreenViewModel::onVitalSignsEvent,
                        getPrecisionFor = registrationScreenViewModel::getPrecisionFor
                    )
                    Tab.RED_CODE -> RedCodeScreen(
                        state = state.triageState,
                        onEvent = registrationScreenViewModel::onTriageEvent,
                    )
                    Tab.YELLOW_CODE -> YellowCodeScreen(
                        state = state.triageState,
                        onEvent = registrationScreenViewModel::onTriageEvent
                    )

                    Tab.ROOM_SELECTION -> RoomScreen(
                        state = state.roomState,
                        onEvent = registrationScreenViewModel::onRoomEvent,
                    )

                    Tab.HISTORY -> VisitHistoryScreen(
                        state = state.visitHistoryState,
                        onEvent = registrationScreenViewModel::onVisitHistoryEvent
                    )
                    Tab.MALNUTRITION_SCREENING -> MalnutritionScreeningScreen(
                        state = state.malnutritionScreeningState,
                        onEvent = registrationScreenViewModel::onMalnutritionScreeningEvent
                    )
                    Tab.SUBMIT_ALL -> SubmissionScreen()
                }
            }

            BottomButtons(
                onCancel = registrationScreenViewModel::onBack,
                onConfirm = onConfirm,
                cancelButtonText = cancelButtonText,
                confirmButtonText = nextButtonText
            )
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
                        registrationScreenViewModel.onNext()
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

enum class Tab(val title: String){
    DEMOGRAPHICS("Demographics"),
    CONTINUE_TO_TRIAGE("Continue to Triage?"),
    VITAL_SIGNS("Vital Signs"),
    RED_CODE("Triage"),
    YELLOW_CODE("Triage"),
    HISTORY("History"),
    ROOM_SELECTION("Room Selection"),
    MALNUTRITION_SCREENING("Malnutrition Screening"),
    SUBMIT_ALL("Submit everything")
}
