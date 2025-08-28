package com.unimib.oases.ui.components.util.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomButtons(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    cancelButtonText: String = "Cancel",
    confirmButtonText: String = "Save"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth().padding(24.dp)
    ) {
        OutlinedButton(
            onClick = {
                onCancel()
            }
        ) {
            Text(cancelButtonText)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = {
                onConfirm()
            }
        ) {
            Text(confirmButtonText)
        }
    }
}