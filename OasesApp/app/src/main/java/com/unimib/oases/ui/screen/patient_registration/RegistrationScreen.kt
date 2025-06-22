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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.ui.components.util.BottomButtons
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoScreen
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.transitionscreens.ContinueToTriageDecisionScreen
import com.unimib.oases.ui.screen.patient_registration.transitionscreens.ReevaluateTriageCodeDecisionScreen
import com.unimib.oases.ui.screen.patient_registration.triage.non_red_code.NonRedCodeScreen
import com.unimib.oases.ui.screen.patient_registration.triage.red_code.RedCodeScreen
import com.unimib.oases.ui.screen.patient_registration.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    padding: PaddingValues
) {

    val registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel()

    val state by registrationScreenViewModel.state.collectAsState()

    val tabs = arrayOf(
        Tabs.Demographics.title,
        Tabs.ContinueToTriage.title,
        Tabs.History.title,
        Tabs.PastMedicalHistory.title,
        Tabs.VitalSigns.title,
        Tabs.ReevaluateTriageCode.title,
        Tabs.Triage.title,
        Tabs.NonRedCode.title
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    var isYellowCodeSelected by remember { mutableStateOf(false) }
    var isRedCodeSelected by remember { mutableStateOf(false) }

    val nextButtonText = remember(currentIndex, isRedCodeSelected) {
        if (currentIndex == tabs.lastIndex) {
            "Submit"
        } else if (tabs[currentIndex] == Tabs.Triage.title && isRedCodeSelected) {
            "Submit"
        } else {
            "Next"
        }
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
                    Tabs.Demographics.title -> PatientInfoScreen(
                        onSubmitted = { patientInfoState ->
                            registrationScreenViewModel.onEvent(RegistrationEvent.PatientSubmitted(patientInfoState))
                            currentIndex++
                        }
                    )
                    Tabs.ContinueToTriage.title -> ContinueToTriageDecisionScreen(
                        onContinueToTriage = { currentIndex++ },
                        onSkipTriage = {
                            navController.popBackStack()
                        }
                    )
                    Tabs.History.title -> VisitHistoryScreen(state.patientInfoState.patient.id)
                    Tabs.PastMedicalHistory.title -> PastHistoryScreen(
                        onSubmitted = { pastHistory ->
                            registrationScreenViewModel.onEvent(RegistrationEvent.PastMedicalHistoryNext(pastHistory))
                            currentIndex++
                        },
                        onBack = { currentIndex-- }
                    )
                    Tabs.VitalSigns.title -> VitalSignsScreen(
                        onSubmitted = { vitalSigns ->
                            registrationScreenViewModel.onEvent(RegistrationEvent.VitalSignsSubmitted(vitalSigns))
                            if (state.currentVisit != null) // If there is an open visit, go to triage reevaluation page
                                currentIndex++
                            else                     // If there is no open visit, go to triage page
                                currentIndex = currentIndex + 2
                        },
                        onBack = { currentIndex-- }
                    )
                    Tabs.ReevaluateTriageCode.title -> ReevaluateTriageCodeDecisionScreen(
                        visit = state.currentVisit!!,
                        onConfirm = { currentIndex++ },
                        onDenial = {
                            registrationScreenViewModel.onEvent(RegistrationEvent.Submit(false))
                            navController.navigate(Screen.HomeScreen.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                    Tabs.Triage.title -> RedCodeScreen(
                        onRedCodeSelected = { redCode ->
                            isRedCodeSelected = redCode
                            if (redCode){
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
                            if (state.currentVisit != null) // There is an open visit, go back to decision page
                                currentIndex--
                            else                      // There is not an open visit, go back to vital signs
                                currentIndex = currentIndex - 2
                        },
                        onSubmitted = {
                            if (isRedCodeSelected){
                                registrationScreenViewModel.onEvent(RegistrationEvent.Submit(true))
                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else
                                currentIndex++
                        },
                        sbpValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Systolic Blood Pressure"}?.value ?: "",
                        dbpValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Diastolic Blood Pressure"}?.value ?: ""
                    )
                    Tabs.NonRedCode.title -> NonRedCodeScreen(
                        onYellowCodeSelected = { yellowCode ->
                            isYellowCodeSelected = yellowCode
                            if (yellowCode){
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
                        ageInt = state.patientInfoState.patient.ageInMonths,
                        spo2Value = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Oxygen Saturation"}?.value ?: "",
                        hrValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Heart Rate"}?.value ?: "",
                        rrValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Respiratory Rate"}?.value ?: "",
                        sbpValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Systolic Blood Pressure"}?.value ?: "",
                        tempValue = state.vitalSignsState.vitalSigns.firstOrNull { it.name == "Temperature"}?.value ?: "",
                    )
                }
            }

            if (tabs[currentIndex] == Tabs.History.title || tabs[currentIndex] == Tabs.NonRedCode.title) {
                BottomButtons(
                    onCancel = { currentIndex-- },
                    onConfirm = {
                        if (currentIndex == tabs.lastIndex) {
                            registrationScreenViewModel.onEvent(RegistrationEvent.Submit(true))
                            navController.navigate(Screen.HomeScreen.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
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
    Demographics("Demographics"),
    ContinueToTriage("Continue to Triage?"),
    History("History"),
    PastMedicalHistory("Past Medical History"),
    VitalSigns("Vital Signs"),
    ReevaluateTriageCode("Reevaluate Triage Code?"),
    Triage("Triage"),
    NonRedCode("Non Red Code")
}
