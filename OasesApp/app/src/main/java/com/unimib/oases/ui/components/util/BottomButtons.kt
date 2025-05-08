package com.unimib.oases.ui.components.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
    cancelButtonText: String = "Cancel",
    confirmButtonText: String = "Save"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
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