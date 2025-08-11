package com.unimib.oases.ui.screen.nurse_assessment

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
import com.unimib.oases.ui.components.util.BottomButtons
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningScreen
import com.unimib.oases.ui.screen.nurse_assessment.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.screen.nurse_assessment.patient_registration.PatientInfoScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.ContinueToTriageDecisionScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.SubmissionScreen
import com.unimib.oases.ui.screen.nurse_assessment.triage.PatientCategory
import com.unimib.oases.ui.screen.nurse_assessment.triage.RedCodeScreen
import com.unimib.oases.ui.screen.nurse_assessment.triage.YellowCodeScreen
import com.unimib.oases.ui.screen.nurse_assessment.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsScreen

fun Array<Tab>.next(currentIndex: Int) = if (currentIndex != this.lastIndex) this[(currentIndex + 1)] else null

fun Array<Tab>.previous(currentIndex: Int) = if (currentIndex != 0) this[(currentIndex - 1)] else null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    padding: PaddingValues,
    authViewModel: AuthViewModel
) {

    val registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel()

    val state by registrationScreenViewModel.state.collectAsState()

    val userRole = authViewModel.currentUser()?.role

    val tabs = arrayOf(
        Tab.DEMOGRAPHICS,
        Tab.CONTINUE_TO_TRIAGE,
        Tab.VITAL_SIGNS,
        Tab.RED_CODE,
        Tab.YELLOW_CODE,
        Tab.HISTORY,
        Tab.PAST_MEDICAL_HISTORY,
        Tab.MALNUTRITION_SCREENING,
        Tab.SUBMIT_ALL
    )

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    var mustSkipPastMedicalHistory = remember {
        derivedStateOf {
            userRole == Role.NURSE && !state.pastHistoryState.hasBeenFilledIn
        }
    }

    val prefixText = remember(currentIndex) {
        if (tabs[currentIndex] == Tab.RED_CODE || tabs[currentIndex] == Tab.YELLOW_CODE){
            when (state.triageState.patientCategory){
                PatientCategory.ADULT -> "Adult "
                PatientCategory.PEDIATRIC -> "Pediatric "
            }
        } else ""
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
                    text = prefixText + tabs[currentIndex].title,
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
                    Tab.DEMOGRAPHICS -> PatientInfoScreen(
                        onSubmitted = { patientInfoState ->
                            registrationScreenViewModel.onEvent(RegistrationEvent.PatientSubmitted(patientInfoState))
                            currentIndex++
                        }
                    )
                    Tab.CONTINUE_TO_TRIAGE -> ContinueToTriageDecisionScreen(
                        onContinueToTriage = {
                            currentIndex++
                        },
                        onSkipTriage = {
                            navController.popBackStack()
                        }
                    )
                    Tab.VITAL_SIGNS -> VitalSignsScreen(
                        onSubmitted = { vitalSigns ->
                            registrationScreenViewModel.onEvent(RegistrationEvent.VitalSignsSubmitted(vitalSigns))
                            currentIndex++
                        },
                        onBack = {
                            currentIndex--
                        }
                    )
                    Tab.RED_CODE -> RedCodeScreen(
                        state = state.triageState,
                        onEvent = registrationScreenViewModel::onTriageEvent,
                    )
                    Tab.YELLOW_CODE -> YellowCodeScreen(
                        state = state.triageState,
                        onEvent = registrationScreenViewModel::onTriageEvent
                    )
                    Tab.PAST_MEDICAL_HISTORY -> PastHistoryScreen(
                        state = state.pastHistoryState,
                        onEvent = registrationScreenViewModel::onPastHistoryEvent,
                        readOnly = userRole == Role.NURSE
                    )
                    Tab.HISTORY -> VisitHistoryScreen(
                        state = state.visitHistoryState,
                        onEvent = registrationScreenViewModel::onVisitHistoryEvent
                    )
                    Tab.MALNUTRITION_SCREENING -> MalnutritionScreeningScreen(
                        state = state.malnutritionScreeningState,
                        onEvent = registrationScreenViewModel::onMalnutritionScreeningEvent
                    )
                    Tab.SUBMIT_ALL -> SubmissionScreen(
                        onSubmit = {
                            registrationScreenViewModel.onEvent(RegistrationEvent.Submit)
                            navController.popBackStack()
                        },
                        onBack = { currentIndex-- }
                    )
                }
            }

            if (tabs[currentIndex] == Tab.HISTORY ||
                tabs[currentIndex] == Tab.YELLOW_CODE ||
                tabs[currentIndex] == Tab.PAST_MEDICAL_HISTORY ||
                tabs[currentIndex] == Tab.RED_CODE ||
                tabs[currentIndex] == Tab.MALNUTRITION_SCREENING

            ) { // Tabs without custom bottom buttons
                BottomButtons(
                    onCancel = {
                        if (tabs.previous(currentIndex) == Tab.YELLOW_CODE && state.triageState.isRedCode ||
                            tabs.previous(currentIndex) == Tab.PAST_MEDICAL_HISTORY && mustSkipPastMedicalHistory.value
                        )
                            currentIndex = currentIndex - 2
                        else
                            currentIndex--
                    },
                    onConfirm = {
                        if (tabs.next(currentIndex) == Tab.PAST_MEDICAL_HISTORY && mustSkipPastMedicalHistory.value ||
                            tabs.next(currentIndex) == Tab.YELLOW_CODE && state.triageState.isRedCode
                        ){
                            currentIndex = currentIndex + 2
                        }
                        else {
                            currentIndex++
                        }
                    },
                    cancelButtonText = "Back",
                    confirmButtonText = "Next"
                )
            }
        }
    }
}

enum class Tab(val title: String){
    DEMOGRAPHICS("Demographics"),
    CONTINUE_TO_TRIAGE("Continue to Triage?"),
    VITAL_SIGNS("Vital Signs"),
    RED_CODE("Triage"),
    YELLOW_CODE("Triage"),
    PAST_MEDICAL_HISTORY("Past Medical History"),
    HISTORY("History"),
    MALNUTRITION_SCREENING("Malnutrition Screening"),
    SUBMIT_ALL("Submit everything")
}
