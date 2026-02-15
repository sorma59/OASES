package com.unimib.oases.ui.screen.nurse_assessment.triage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.domain.model.VisitVitalSign
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form.InputFields
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form.VitalSignsFormEvent
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form.VitalSignsFormState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form.VitalSignsFormViewModel
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.util.reactToKeyboardAppearance

@Composable
fun TriageScreen(appViewModel: AppViewModel) {

    val viewModel: TriageViewModel = hiltViewModel()

    val vitalSignsViewModel: VitalSignsFormViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val vitalSignsState by vitalSignsViewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    TriageContent(
        state,
        viewModel::onEvent,
        vitalSignsState,
        vitalSignsViewModel::onEvent,
        vitalSignsViewModel::getVisitVitalSigns,
        vitalSignsViewModel::getPrecisionFor,
        Modifier.fillMaxHeight()
    )
}

@Composable
private fun TriageContent(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
    vitalSignsState: VitalSignsFormState,
    onVitalSignsEvent: (VitalSignsFormEvent) -> Unit,
    getVisitVitalSigns: () -> List<VisitVitalSign>,
    getPrecisionFor: (String) -> NumericPrecision?,
    modifier: Modifier = Modifier
) {

    val onDismiss = remember {
        { onEvent(TriageEvent.DismissDialog) }
    }

    val onConfirm = remember {
        {
            onEvent(
                TriageEvent.ConfirmDialog(
                    getVisitVitalSigns()
                )
            )
        }
    }

    state.error?.let {
        RetryButton(
            error = it,
            onClick = { onEvent(TriageEvent.Retry) }
        )
    } ?: if (state.uiMode is PatientRegistrationScreensUiMode.Standalone && !state.uiMode.isEditing)
        TriageSummaryContent(state, onEvent, Modifier.padding(16.dp))  // View mode
    else {
        state.editingState?.let { editingState ->
            Box {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.reactToKeyboardAppearance()
                ) {
                    Box(
                        Modifier.weight(1f)
                    ) {
                        when (editingState.currentTab) {
                            TriageTab.REDS -> RedCodeContent(state, onEvent)
                            TriageTab.YELLOWS -> YellowCodeContent(state, onEvent)
                            TriageTab.VITAL_SIGNS -> InputFields(
                                vitalSignsState,
                                onVitalSignsEvent,
                                getPrecisionFor,

                            )

                            TriageTab.ROOM -> RoomContent(editingState, onEvent)
                        }
                    }

                    BottomButtons(
                        onCancel = { onEvent(TriageEvent.BackButtonPressed) },
                        onConfirm = {
                            onEvent(
                                TriageEvent.NextButtonPressed(
                                    getVisitVitalSigns().takeIf {
                                        state.editingState.currentTab == TriageTab.VITAL_SIGNS
                                    }
                                )
                            )
                        },
                        cancelButtonText = editingState.cancelButtonText,
                        confirmButtonText = editingState.nextButtonText,
                    )
                }

                if (state.showAlertDialog) {
                    AlertDialog(
                        onDismissRequest = onDismiss,
                        title = {
                            Text("Confirm triage data saving")
                        },
                        text = {
                            Text(text = "Do you want to save the triage data to the database?")
                        },
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
    }
}