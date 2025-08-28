package com.unimib.oases.ui.screen.bluetooth.sending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.bluetooth.devices.DeviceList
import com.unimib.oases.ui.components.patients.PatientItem
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.util.Resource


@Composable
fun SendPatientViaBluetoothScreen(
    patient: Patient,
    navController: NavController,
    padding: PaddingValues
) {

    var showResultDialog by remember { mutableStateOf(false) }
    var resultMessageToShow by remember { mutableStateOf("") }

    val sendPatientViaBluetoothViewModel: SendPatientViaBluetoothViewModel = hiltViewModel()

    val pairedDevices = sendPatientViaBluetoothViewModel.pairedDevices.collectAsState()

    val sendPatientResult = sendPatientViaBluetoothViewModel.sendPatientResult.collectAsState()

    // Define the function to show the result dialog
    fun showResult(message: String) {
        resultMessageToShow = message
        showResultDialog = true
    }

    LaunchedEffect(sendPatientResult.value) {
        when (sendPatientResult.value) {
            is Resource.Success -> {
                showResult("Patient sent successfully, tap another device to send ${patient.name} again")
                sendPatientViaBluetoothViewModel.onEvent(SendPatientViaBluetoothEvent.SendResultShown)
            }
            is Resource.Error -> {
                showResult("Failed to send patient:\n ${(sendPatientResult.value as Resource.Error).message}")
                sendPatientViaBluetoothViewModel.onEvent(SendPatientViaBluetoothEvent.SendResultShown)
            }
            else -> {
                // Do nothing for Loading, None, or other states
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .consumeWindowInsets(padding)
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.height(100.dp)
                ) {
                    Text("Patient:")

                    PatientItem(
                        patient = patient,
                        navController = navController,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        hideBluetoothButton = true,
                        onClick = { navController.popBackStack() }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TitleText("Select the receiver", Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    DeviceList(
                        devices = pairedDevices.value,
                        devicesType = "Paired Devices",
                        onClick = {
                            sendPatientViaBluetoothViewModel.onEvent(
                                SendPatientViaBluetoothEvent.SendPatient(patient, it)
                            )
                        },
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ){

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate(Screen.PairDevice.route) }
            ){ Text("Need to pair a new device?") }

            Box (
                modifier = Modifier.weight(1f)
            ){
                sendPatientViaBluetoothViewModel.sendPatientResult.collectAsState().value.let {
                    when (it) {
                        is Resource.Loading<*> -> CustomCircularProgressIndicator()
                        else -> {}
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
            onDismiss = { showResultDialog = false }
        )
    }
}

@Composable
fun ResultDialog(
    resultMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Handles dismiss on tap outside/back press
        text = {
            // Display the result message
            Text(resultMessage)
        },
        confirmButton = {
            // Optional: An explicit dismiss button
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
        // No dismissButton is needed if confirmButton acts as dismiss
    )
}
