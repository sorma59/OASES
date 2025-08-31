package com.unimib.oases.ui.screen.bluetooth.sending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.ui.components.bluetooth.devices.DeviceList
import com.unimib.oases.ui.components.patients.PatientItem
import com.unimib.oases.ui.components.scaffold.OasesTopAppBar
import com.unimib.oases.ui.components.scaffold.OasesTopAppBarType
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.util.ToastUtils


@Composable
fun SendPatientViaBluetoothScreen(
    navController: NavController,
    padding: PaddingValues
) {

    var showResultDialog by remember { mutableStateOf(false) }
    var resultMessageToShow by remember { mutableStateOf(null as String?) }

    val sendPatientViaBluetoothViewModel: SendPatientViaBluetoothViewModel = hiltViewModel()

    val state by sendPatientViaBluetoothViewModel.state.collectAsState()

    val context = LocalContext.current

    val onDialogDismiss = {
        if (!state.patientSendingState.isLoading) { // Cannot dismiss if loading
            showResultDialog = false
            resultMessageToShow = null
        }
    }

    LaunchedEffect(state.toastMessage) {

        state.toastMessage?.let{
            ToastUtils.showToast(context, it)
            sendPatientViaBluetoothViewModel.onEvent(SendPatientViaBluetoothEvent.OnToastShown)
        }

    }

    LaunchedEffect(state.patientSendingState.isLoading) {
        if (state.patientSendingState.isLoading)
            showResultDialog = true
        resultMessageToShow = state.patientSendingState.result
    }

    Column(
        Modifier.fillMaxSize()
    ){
        OasesTopAppBar(
            title = "Send Patient",
            type = OasesTopAppBarType.BACK,
            onNavigationIconClick = { navController.popBackStack() }
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    Spacer(modifier = Modifier.height(32.dp))

                    PatientItem(
                        patient = state.patient,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = {
                            sendPatientViaBluetoothViewModel.onEvent(SendPatientViaBluetoothEvent.PatientItemClicked)
                        },
                        errorText = "Couldn't load patient data, tap to retry \n${state.patientRetrievalState.error}",
                        isLoading = state.patientRetrievalState.isLoading
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TitleText("Select the receiver", Modifier.padding(horizontal = 16.dp))

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ){
                            DeviceList(
                                devices = state.pairedDevices,
                                devicesType = "Paired Devices",
                                onClick = { device ->
                                    sendPatientViaBluetoothViewModel.onEvent(
                                        SendPatientViaBluetoothEvent.SendPatient(device)
                                    )
                                },
                                modifier = Modifier.weight(1f, false)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            TextButton(
                                onClick = { navController.navigate(Screen.PairDevice.route) }
                            ) { Text("Need to pair a new device?") }
                        }
                    }
                }
            }

            BottomButtons(
                onCancel = { navController.popBackStack() },
                onConfirm = { navController.popBackStack() },
                confirmButtonText = "Done"
            )
        }
    }

    if (showResultDialog) {
        ResultDialog(
            resultMessage = resultMessageToShow,
            onDismiss = onDialogDismiss
        )
    }
}

@Composable
fun ResultDialog(
    resultMessage: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Handles dismiss on tap outside/back press
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                resultMessage?.let {
                    Text(resultMessage)
                } ?: run {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        Text("Sending...")
                        CircularProgressIndicator()
                    }
                }
            }
        },
        confirmButton = {
            resultMessage?.let {
                TextButton(onClick = onDismiss) {
                    Text("Ok")
                }
            }
        }
        // No dismissButton is needed if confirmButton acts as dismiss
    )
}
