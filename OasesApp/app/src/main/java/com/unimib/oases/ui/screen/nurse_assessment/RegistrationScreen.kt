package com.unimib.oases.ui.screen.nurse_assessment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.Companion.DEMOGRAPHICS_COMPLETED_KEY
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreenViewModel.Companion.STEP_COMPLETED_KEY
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.ContinueToMalnutritionDecisionScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.ContinueToTriageDecisionScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.StartWithDemographicsDecisionScreen
import com.unimib.oases.ui.screen.nurse_assessment.transitionscreens.SubmissionScreen
import com.unimib.oases.ui.screen.root.AppViewModel
import kotlin.reflect.KClass

@Composable
fun RegistrationScreen(
    appViewModel: AppViewModel,
    navController: NavController
) {

    val registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel()

    val state by registrationScreenViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        registrationScreenViewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>(STEP_COMPLETED_KEY)
            ?.asFlow()
            ?.collect { completed ->
                if (completed) {
                    registrationScreenViewModel.onEvent(RegistrationEvent.StepCompleted)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        // IMPORTANT: Reset the value in the SavedStateHandle so this
                        // doesn't trigger again on configuration change.
                        ?.set(STEP_COMPLETED_KEY, false)
                }
            }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<PatientAndVisitIds?>(DEMOGRAPHICS_COMPLETED_KEY)
            ?.asFlow()
            ?.collect { ids ->
                ids?.let {
                    registrationScreenViewModel.onEvent(
                        RegistrationEvent.PatientAndVisitCreated(
                            it.patientId, it.visitId
                        )
                    )
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        // Remove the mapping since it is a one timer
                        ?.remove<PatientAndVisitIds?>(DEMOGRAPHICS_COMPLETED_KEY)
                }
            }
    }

    RegistrationContent(
        state,
        registrationScreenViewModel::onEvent,
        registrationScreenViewModel::onNext,
        registrationScreenViewModel::onBack
    )
}

@Composable
fun RegistrationContent(
    state: RegistrationState,
    onEvent: (RegistrationEvent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    val nextButtonText = remember(state.currentTab) {
        when (state.currentTab) {
            Tab.START_WITH_DEMOGRAPHICS_DECISION -> "Go"
            Tab.CONTINUE_TO_TRIAGE -> "Triage"
            Tab.CONTINUE_TO_MALNUTRITION_SCREENING -> "Malnutrition Screening"
            Tab.SUBMIT_ALL -> "Submit"
        }
    }

    val cancelButtonText = remember(state.currentTab) {
        when (state.currentTab) {
            Tab.START_WITH_DEMOGRAPHICS_DECISION -> "Cancel"
            Tab.CONTINUE_TO_TRIAGE, Tab.CONTINUE_TO_MALNUTRITION_SCREENING -> "Home"
            else -> "Back"
        }
    }

    state.error?.let {
        RetryButton(
            it,
            onClick = { onEvent(RegistrationEvent.Retry) }
        )
    } ?:

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
                    text = state.currentTab.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (state.currentTab) {

                    Tab.START_WITH_DEMOGRAPHICS_DECISION -> StartWithDemographicsDecisionScreen()

                    Tab.CONTINUE_TO_TRIAGE -> ContinueToTriageDecisionScreen()

                    Tab.CONTINUE_TO_MALNUTRITION_SCREENING -> ContinueToMalnutritionDecisionScreen()

                    Tab.SUBMIT_ALL -> SubmissionScreen()
                }
            }

            BottomButtons(
                onCancel = onBack,
                onConfirm = onNext,
                cancelButtonText = cancelButtonText,
                confirmButtonText = nextButtonText
            )
        }
    }
}

enum class Tab(val title: String, val destinationClass: KClass<out Route>?){
    START_WITH_DEMOGRAPHICS_DECISION("First step: Demographics", Route.Demographics::class),
    CONTINUE_TO_TRIAGE("Continue to Triage?", Route.Triage::class),
    CONTINUE_TO_MALNUTRITION_SCREENING("Continue to Malnutrition Screening?", Route.MalnutritionScreening::class),
    SUBMIT_ALL("Submit everything", null)
}