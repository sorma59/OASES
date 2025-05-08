package com.fiax.hdr.ui.components.bluetooth.devices

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
@SuppressLint("MissingPermission")
fun DeviceItem(device: BluetoothDevice, onClick: (BluetoothDevice) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(device) },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RectangleShape,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = device.name ?: "Unknown Device",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            if (device.name == null)
                Text(
                    text = device.address,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
        }
    }

}