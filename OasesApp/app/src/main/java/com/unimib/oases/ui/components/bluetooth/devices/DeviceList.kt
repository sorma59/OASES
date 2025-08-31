package com.unimib.oases.ui.components.bluetooth.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.fiax.hdr.ui.components.bluetooth.devices.DeviceItem
import com.unimib.oases.ui.components.util.SmallGrayText
import kotlinx.coroutines.launch

@Composable
fun DeviceList(
    onClick: (BluetoothDevice) -> Unit,
    devices: List<BluetoothDevice>,
    devicesType: String,
    modifier: Modifier = Modifier,
) {

    val scrollState = rememberScrollState() // Remember the scroll state
    val coroutineScope = rememberCoroutineScope() // Coroutine scope for scrolling

    // Determine if the user is at the bottom of the scrollable content
    val isAtBottom by remember {
        derivedStateOf {
            // Check if the current scroll position is close to the maximum scroll extent
            // Allow for a small tolerance
            val tolerance = 20.dp.value // Convert dp tolerance to pixels

            scrollState.value >= scrollState.maxValue - tolerance
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmallGrayText(devicesType, Modifier.align(Alignment.Start).padding(start = 8.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // The scrollable Column
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth() // Fill the Box
                    .verticalScroll(scrollState), // Apply the vertical scroll modifier
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (devices.isNotEmpty()) {

                    devices.forEach { device ->
                        DeviceItem(device, onClick)
                    }
                } else {
                    // Display the "No devices found" message

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "No devices found",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp),
                        )
                    }
                }
            }

            // The Scroll to End Button (positioned within the Box)
            if (!isAtBottom) { // Show the button only when not at the bottom
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            // Scroll to the maximum scroll extent (the very end)
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors( // Use the colors parameter
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow, // Set the container color (background of the button area)
                        contentColor = MaterialTheme.colorScheme.onSurface // Set the color of the icon and ripple effect
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.ArrowDownward, contentDescription = "Scroll to End")
                }
            }
        }
    }
}