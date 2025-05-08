package com.fiax.hdr.ui.components.bluetooth.devices

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.util.SmallGrayText

@Composable
fun DeviceList(
    onClick: (BluetoothDevice) -> Unit,
    devices: List<BluetoothDevice>,
    devicesType: String,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmallGrayText(devicesType, Modifier.align(Alignment.Start))

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (devices.isNotEmpty()){
                items(devices) { device ->
                    DeviceItem(device, onClick)
                }
            } else {
                item {
                    Card(modifier = Modifier.fillMaxWidth()){
                        Text(
                            text ="No devices found",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}