package com.unimib.oases.ui.components.util.button

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DismissButton(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "Cancel",
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors()
){
    OutlinedButton(
        onClick = onDismiss,
        modifier = modifier,
        colors = colors
    ) {
        Text(buttonText)
    }
}