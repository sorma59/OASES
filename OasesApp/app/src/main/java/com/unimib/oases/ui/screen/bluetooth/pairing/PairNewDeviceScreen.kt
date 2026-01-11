package com.unimib.oases.ui.screen.bluetooth.pairing

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
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.loading.SmallCircularProgressIndicator
import com.unimib.oases.ui.util.ToastUtils

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
            ToastUtils.showToast(context, toastMessage)
    }

    LaunchedEffect(pairingResult) {
        if (pairingResult.isNotEmpty())
            ToastUtils.showToast(context, toastMessage)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ){

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                                onClick = { pairNewDeviceScreenViewModel.onEvent(PairNewDeviceEvent.MakeDeviceDiscoverable()) },
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
                                        pairNewDeviceScreenViewModel.onEvent(PairNewDeviceEvent.StartScan)
                                    else
                                        pairNewDeviceScreenViewModel.onEvent(PairNewDeviceEvent.StopScan)
                                },
                            ) { Text(if (!isDiscovering) "Scan" else "Stop") }
                        }

                    }

                    CenteredText("Make sure the device you want to pair with is visible to nearby devices.")

                    if (timeLeft != null)
                        CenteredText(text = "Your device ($deviceName) is visible to nearby devices for $timeLeft")
                    else
                        CenteredText(text = "Your device ($deviceName) is not visible to nearby devices")

                }

                DeviceList(
                    devices = discoveredDevices,
                    devicesType = "Discovered Devices",
                    onClick = {
                        pairNewDeviceScreenViewModel.onEvent(
                            PairNewDeviceEvent.PairDevice(
                                it
                            )
                        )
                    },
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
}