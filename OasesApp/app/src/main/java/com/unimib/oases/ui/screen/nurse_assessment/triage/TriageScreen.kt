package com.unimib.oases.ui.screen.nurse_assessment.triage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.NumericPrecision
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsContent
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsEvent
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsState
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsViewModel
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.ToastUtils
import com.unimib.oases.util.reactToKeyboardAppearance

@Composable
fun TriageScreen(appViewModel: AppViewModel) {

    val viewModel: TriageViewModel = hiltViewModel()

    val vitalSignsViewModel: VitalSignsViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val vitalSignsState by vitalSignsViewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            ToastUtils.showToast(context, it)
            viewModel.onEvent(TriageEvent.ToastShown)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    LaunchedEffect(Unit) {
        vitalSignsViewModel.onEvent(VitalSignsEvent.Retry)
    }

    TriageContent(
        state,
        viewModel::onEvent,
        vitalSignsState,
        vitalSignsViewModel::onEvent,
        vitalSignsViewModel::getPrecisionFor,
        Modifier.fillMaxHeight()
    )
}

@Composable
private fun TriageContent(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
    vitalSignsState: VitalSignsState,
    onVitalSignsEvent: (VitalSignsEvent) -> Unit,
    getPrecisionFor: (String) -> NumericPrecision?,
    modifier: Modifier = Modifier
) {

    val onDismiss = remember {
        { onEvent(TriageEvent.DismissDialog) }
    }

    val onConfirm = remember {
        { onEvent(TriageEvent.ConfirmDialog) }
    }

    state.error?.let {
        RetryButton(
            error = it,
            onClick = { onEvent(TriageEvent.Retry) }
        )
    } ?: if (state.isLoading)
        CustomCircularProgressIndicator()
    else {
        if (state.uiMode is PatientRegistrationScreensUiMode.Standalone && !state.uiMode.isEditing)
        // View mode
            TriageSummaryContent(state, onEvent)
        else {
            Box {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.reactToKeyboardAppearance()
                ) {
                    Box(
                        Modifier.weight(1f)
                    ) {
                        when (state.editingState!!.currentTab) {
                            TriageTab.REDS -> RedCodeContent(state, onEvent)
                            TriageTab.YELLOWS -> YellowCodeContent(state, onEvent)
                            TriageTab.VITAL_SIGNS -> VitalSignsContent(
                                vitalSignsState,
                                onVitalSignsEvent,
                                getPrecisionFor
                            )

                            TriageTab.ROOM -> RoomContent(state.editingState, onEvent)
                        }
                    }

                    BottomButtons(
                        onCancel = { onEvent(TriageEvent.BackButtonPressed) },
                        onConfirm = { onEvent(TriageEvent.NextButtonPressed) },
                        cancelButtonText = state.editingState!!.cancelButtonText,
                        confirmButtonText = state.editingState.nextButtonText,
                    )
                }

                if (state.showAlertDialog) {
                    AlertDialog(
                        onDismissRequest = onDismiss,
                        title = {
                            Text("Confirm triage data saving")
                        },
                        text = {
                            state.savingState.error?.let {
                                Text("Error: $it")
                            } ?: if (state.savingState.isLoading)
                                CustomCircularProgressIndicator()
                            else
                                Text(text = "Do you want to save the triage data to the database?")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = onConfirm
                            ) {
                                Text(if (state.savingState.error == null) "Confirm" else "Retry")
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