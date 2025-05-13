package com.unimib.oases.ui.screen.bluetooth.pairing

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.ui.components.bluetooth.devices.DeviceList
import com.unimib.oases.ui.components.util.BottomButtons
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.circularprogressindicator.SmallCircularProgressIndicator

@Composable
fun PairNewDeviceScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val pairNewDeviceScreenViewModel: PairNewDeviceScreenViewModel = hiltViewModel()

    val toastMessage by pairNewDeviceScreenViewModel.toastMessage.collectAsState()

    val pairingResult by pairNewDeviceScreenViewModel.pairingResult.collectAsState()

    val discoveredDevices by pairNewDeviceScreenViewModel.discoveredDevices.collectAsState()

    val isDiscovering by pairNewDeviceScreenViewModel.isDiscovering.collectAsState()

    val timeLeft by pairNewDeviceScreenViewModel.remainingTime.collectAsState()

    val deviceName = pairNewDeviceScreenViewModel.deviceName

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty())
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(pairingResult) {
        if (pairingResult.isNotEmpty())
            Toast.makeText(context, pairingResult, Toast.LENGTH_SHORT).show()
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = { pairNewDeviceScreenViewModel.makeDeviceDiscoverable(30) },
                            enabled = timeLeft == null,
                        ) {
                            Text(
                                text = if (timeLeft == null) "Make this device visible to others" else "Currently visible"
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        VerticalDivider(Modifier.fillMaxHeight())

                        if (isDiscovering) {
                            Spacer(Modifier.width(4.dp))
                            SmallCircularProgressIndicator()
                        }
                        TextButton(
                            onClick = {
                                if (!isDiscovering)
                                    pairNewDeviceScreenViewModel.startScan()
                                else
                                    pairNewDeviceScreenViewModel.stopScan()
                            },
                        ) { Text(if (!isDiscovering) "Scan" else "Stop") }
                    }

                }

                Spacer(modifier = Modifier.height(32.dp))

                CenteredText("Make sure the device you want to pair with is visible to nearby devices.")

                Spacer(modifier = Modifier.height(32.dp))

                if (timeLeft != null)
                    CenteredText(text = "Your device ($deviceName) is visible to nearby devices for $timeLeft")
                else
                    CenteredText(text = "Your device ($deviceName) is not visible to nearby devices")

                Spacer(modifier = Modifier.height(32.dp))
            }

            DeviceList(
                devices = discoveredDevices,
                devicesType = "Discovered Devices",
                onClick = { pairNewDeviceScreenViewModel.pairDevice(it) },
                modifier = Modifier.weight(1f)
            )
        }

        BottomButtons(
            onCancel = { navController.popBackStack() },
            onConfirm = { navController.popBackStack() },
            confirmButtonText = "Done"
        )
    }
}