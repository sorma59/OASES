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
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.domain.model.symptom.PatientCategory
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.ValidationEvent
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningContent
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningEvent
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningState
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoContent
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoEvent
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoState
import com.unimib.oases.ui.screen.nurse_assessment.room_selection.RoomContent
import com.unimib.oases.ui.screen.nurse_assessment.room_selection.RoomEvent
import com.unimib.oases.ui.screen.nurse_assessment.room_selection.RoomState
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.ContinueToTriageDecisionScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.SubmissionScreen
import com.unimib.oases.ui.screen.nurse_assessment.triage.RedCodeContent
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageEvent
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageState
import com.unimib.oases.ui.screen.nurse_assessment.triage.YellowCodeContent
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryContent
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryEvent
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsContent
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsEvent
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsState
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun RegistrationScreen(
    appViewModel: AppViewModel
) {

    val registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel()

    val state by registrationScreenViewModel.state.collectAsState()

    val validationEvents = registrationScreenViewModel.validationEvents

    val context = LocalContext.current

    var showAlertDialog by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        registrationScreenViewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    RegistrationContent(
        state.currentTab,
        state.patientInfoState,
        state.vitalSignsState,
        state.triageState,
        state.roomState,
        state.visitHistoryState,
        state.malnutritionScreeningState,
        registrationScreenViewModel::onPatientInfoEvent,
        registrationScreenViewModel::onVitalSignsEvent,
        registrationScreenViewModel::onTriageEvent,
        registrationScreenViewModel::onRoomEvent,
        registrationScreenViewModel::onVisitHistoryEvent,
        registrationScreenViewModel::onMalnutritionScreeningEvent,
        registrationScreenViewModel::getPrecisionFor,
        registrationScreenViewModel::onNext,
        registrationScreenViewModel::onBack,
        showAlertDialog,
        {
            showAlertDialog = false
        },
        {
            showAlertDialog = false
            registrationScreenViewModel.onNext()
        }
    )
}

@Composable
fun RegistrationContent(
    currentTab: Tab,
    patientInfoState: PatientInfoState,
    vitalSignsState: VitalSignsState,
    triageState: TriageState,
    roomState: RoomState,
    visitHistoryState: VisitHistoryState,
    malnutritionScreeningState: MalnutritionScreeningState,
    onPatientInfoEvent: (PatientInfoEvent) -> Unit,
    onVitalSignsEvent: (VitalSignsEvent) -> Unit,
    onTriageEvent: (TriageEvent) -> Unit,
    onRoomEvent: (RoomEvent) -> Unit,
    onVisitHistoryEvent: (VisitHistoryEvent) -> Unit,
    onMalnutritionScreeningEvent: (MalnutritionScreeningEvent) -> Unit,
    getPrecisionFor: (String) -> NumericPrecision?,
    onNext: () -> Unit,
    onBack: () -> Unit,
//    onConfirm: () -> Unit,
    showAlertDialog: Boolean,
    onDismissAlertDialog: () -> Unit,
    onConfirmAlertDialog: () -> Unit
) {

    val prefixText = remember(currentTab) {
        when (currentTab) {
            Tab.RED_CODE, Tab.YELLOW_CODE -> {
                when (triageState.patientCategory) {
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
                    if (patientInfoState.isEdited || patientInfoState.isNew)
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


    val cancelButtonText = remember(currentTab) {
        when (currentTab) {
            Tab.DEMOGRAPHICS -> "Cancel"
            Tab.CONTINUE_TO_TRIAGE -> "Home"
            else -> "Back"
        }
    }

    val onConfirm by remember(
        currentTab,
        patientInfoState.isEdited,
        patientInfoState.isNew
    ) {
        derivedStateOf {
            if (currentTab == Tab.DEMOGRAPHICS &&
                (patientInfoState.isEdited ||
                        patientInfoState.isNew)
            ) {
                { onPatientInfoEvent(PatientInfoEvent.NextButtonPressed) }
            } else
                onNext
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
                    Tab.DEMOGRAPHICS -> PatientInfoContent(
                        state = patientInfoState,
                        onEvent = onPatientInfoEvent
                    )

                    Tab.CONTINUE_TO_TRIAGE -> ContinueToTriageDecisionScreen()
                    Tab.VITAL_SIGNS -> VitalSignsContent(
                        state = vitalSignsState,
                        onEvent = onVitalSignsEvent,
                        getPrecisionFor = getPrecisionFor
                    )

                    Tab.RED_CODE -> RedCodeContent(
                        state = triageState,
                        onEvent = onTriageEvent,
                    )

                    Tab.YELLOW_CODE -> YellowCodeContent(
                        state = triageState,
                        onEvent = onTriageEvent
                    )

                    Tab.ROOM_SELECTION -> RoomContent(
                        state = roomState,
                        onEvent = onRoomEvent,
                    )

                    Tab.HISTORY -> VisitHistoryContent(
                        state = visitHistoryState,
                        onEvent = onVisitHistoryEvent
                    )

                    Tab.MALNUTRITION_SCREENING -> MalnutritionScreeningContent(
                        state = malnutritionScreeningState,
                        onEvent = onMalnutritionScreeningEvent
                    )

                    Tab.SUBMIT_ALL -> SubmissionScreen()
                }
            }

            BottomButtons(
                onCancel = onBack,
                onConfirm = onConfirm,
                cancelButtonText = cancelButtonText,
                confirmButtonText = nextButtonText
            )
        }
    }

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = onDismissAlertDialog,
            title = {
                Text(text = "Confirm patient saving")
            },
            text = {
                Text(text = "Do you want to save the patient to the database?")
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmAlertDialog
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissAlertDialog
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