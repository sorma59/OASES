package com.unimib.oases.ui.screen.patient_registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.ui.components.util.BottomButtons
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoScreen
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.transitionscreens.ContinueToTriageDecisionScreen
import com.unimib.oases.ui.screen.patient_registration.triage.non_red_code.NonRedCodeScreen
import com.unimib.oases.ui.screen.patient_registration.triage.red_code.RedCodeScreen
import com.unimib.oases.ui.screen.patient_registration.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    padding: PaddingValues,
    authViewModel: AuthViewModel
) {

    val registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel()
    val pastHistoryState = registrationScreenViewModel.state.collectAsState().value.pastHistoryState

    val state by registrationScreenViewModel.state.collectAsState()

    val userRole = authViewModel.currentUser()?.role

    val tabs = arrayOf(
        Tabs.DEMOGRAPHICS.title,
        Tabs.CONTINUE_TO_TRIAGE.title,
        Tabs.VITAL_SIGNS.title,
        Tabs.TRIAGE.title,
        Tabs.NON_RED_CODE.title,
        Tabs.HISTORY.title,
        Tabs.PAST_MEDICAL_HISTORY.title,
    )

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    var isYellowCode by remember { mutableStateOf(false) }
    var isRedCode by remember { mutableStateOf(false) }
    var mustSkipPastMedicalHistory = remember {
        derivedStateOf {
            userRole == Role.NURSE && !state.pastHistoryState.hasBeenFilledIn
        }
    }

    val nextButtonText = remember(currentIndex) {
        if (currentIndex == tabs.lastIndex ||
            (mustSkipPastMedicalHistory.value && currentIndex == tabs.lastIndex - 1))
            "Submit"
        else
            "Next"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Patient Registration",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(Screen.HomeScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Arrow back"
                    )
                }
            },
            actions = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tabs[currentIndex],
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (tabs[currentIndex]) {
                    Tabs.DEMOGRAPHICS.title -> PatientInfoScreen(
                        onSubmitted = { patientInfoState ->
                            registrationScreenViewModel.onEvent(RegistrationEvent.PatientSubmitted(patientInfoState))
                            currentIndex++
                        }
                    )
                    Tabs.CONTINUE_TO_TRIAGE.title -> ContinueToTriageDecisionScreen(
                        onContinueToTriage = {
                            currentIndex++
                        },
                        onSkipTriage = {
                            navController.popBackStack()
                        }
                    )
                    Tabs.VITAL_SIGNS.title -> VitalSignsScreen(
                        onSubmitted = { vitalSigns ->
                            registrationScreenViewModel.onEvent(RegistrationEvent.VitalSignsSubmitted(vitalSigns))
                            currentIndex++
                        },
                        onBack = {
                            currentIndex--
                        }
                    )
                    Tabs.TRIAGE.title -> RedCodeScreen(
                        onRedCodeToggle = { isRedCodeSelected ->
                            isRedCode = isRedCodeSelected
                            if (isRedCode){
                                registrationScreenViewModel.onEvent(
                                    RegistrationEvent.TriageCodeSelected(
                                        TriageCode.RED.name
                                    )
                                )
                            } else
                                registrationScreenViewModel.onEvent(
                                    RegistrationEvent.TriageCodeSelected(
                                        TriageCode.GREEN.name
                                    )
                                )
                        },
                        onBack = {
                            currentIndex--
                        },
                        onSubmitted = {
                            if (isRedCode){ // Red code: skip yellow code
                                currentIndex = currentIndex + 2
                            } else                  // Not a red code, check for yellow code
                                currentIndex++
                        },
                        sbpValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Systolic Blood Pressure"}?.value ?: "",
                        dbpValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Diastolic Blood Pressure"}?.value ?: ""
                    )
                    Tabs.NON_RED_CODE.title -> NonRedCodeScreen(
                        onYellowCodeToggle = { isYellowCodeSelected ->
                            isYellowCode = isYellowCodeSelected
                            if (isYellowCode){
                                registrationScreenViewModel.onEvent(
                                    RegistrationEvent.TriageCodeSelected(
                                        TriageCode.YELLOW.name
                                    )
                                )
                            } else
                                registrationScreenViewModel.onEvent(
                                    RegistrationEvent.TriageCodeSelected(
                                        TriageCode.GREEN.name
                                    )
                                )
                        },
                        ageInMonths = state.patientInfoState.patient.ageInMonths,
                        spo2Value = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Oxygen Saturation"}?.value ?: "",
                        hrValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Heart Rate"}?.value ?: "",
                        rrValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Respiratory Rate"}?.value ?: "",
                        sbpValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Systolic Blood Pressure"}?.value ?: "",
                        tempValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Temperature"}?.value ?: "",
                    )
                    Tabs.HISTORY.title -> VisitHistoryScreen(
                        state.visitHistoryState,
                        registrationScreenViewModel::onVisitHistoryEvent
                    )
                    Tabs.PAST_MEDICAL_HISTORY.title ->
                        PastHistoryScreen(
                            state = pastHistoryState,
                            onEvent = registrationScreenViewModel::onPastHistoryEvent,
                            onSubmitted = {
                                registrationScreenViewModel.onEvent(RegistrationEvent.Submit)
                                navController.popBackStack()
                            },
                            onBack = { currentIndex-- },
                            confirmButtonText = nextButtonText,
                            readOnly = userRole == Role.NURSE
                        )
                }
            }

            if (tabs[currentIndex] == Tabs.HISTORY.title || tabs[currentIndex] == Tabs.NON_RED_CODE.title) {
                BottomButtons(
                    onCancel = {
                        if (tabs[currentIndex] == Tabs.HISTORY.title && isRedCode)
                            currentIndex = currentIndex - 2
                        else
                            currentIndex--
                    },
                    onConfirm = {
                        if (currentIndex == tabs.lastIndex) {
                            registrationScreenViewModel.onEvent(RegistrationEvent.Submit)
                            navController.popBackStack()
                        }
                        else if (tabs[currentIndex] == Tabs.HISTORY.title && mustSkipPastMedicalHistory.value){
                            registrationScreenViewModel.onEvent(RegistrationEvent.Submit)
                            navController.popBackStack()
                        }
                        else {
                            currentIndex++
                        }
                    },
                    cancelButtonText = "Back",
                    confirmButtonText = nextButtonText
                )
            }
        }
    }
}

enum class Tabs(val title: String){
    DEMOGRAPHICS("Demographics"),
    CONTINUE_TO_TRIAGE("Continue to Triage?"),
    VITAL_SIGNS("Vital Signs"),
    TRIAGE("Triage"),
    NON_RED_CODE("Non Red Code"),
    PAST_MEDICAL_HISTORY("Past Medical History"),
    HISTORY("History"),
}
