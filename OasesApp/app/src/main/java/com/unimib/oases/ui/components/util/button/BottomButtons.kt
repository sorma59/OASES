package com.unimib.oases.ui.components.util.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.util.LocalWindowSizeClass
import com.unimib.oases.util.let2

@Composable
fun BottomButtons(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    cancelButtonText: String = "Cancel",
    confirmButtonText: String = "Save"
) {
    val windowSize = LocalWindowSizeClass.current.widthSizeClass

    val width = when(windowSize) {
        WindowWidthSizeClass.Compact -> null
        WindowWidthSizeClass.Medium -> 120.dp
        WindowWidthSizeClass.Expanded -> 160.dp
        else -> null
    }

    val height = when(windowSize) {
        WindowWidthSizeClass.Compact -> null
        WindowWidthSizeClass.Medium -> 60.dp
        WindowWidthSizeClass.Expanded -> 80.dp
        else -> null
    }

    val textSize = when(windowSize) {
        WindowWidthSizeClass.Compact -> null
        WindowWidthSizeClass.Medium -> 22.sp
        WindowWidthSizeClass.Expanded -> 26.sp
        else -> null
    }
    val actualModifier = let2(width, height) { w, h ->
        Modifier.size(width = w, height = h)
    } ?: Modifier

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth().padding(24.dp)
    ) {
        OutlinedButton(
            modifier = actualModifier,
            onClick = {
                onCancel()
            }
        ) {
            Text(
                text = cancelButtonText,
                fontSize = textSize ?: TextUnit.Unspecified
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            modifier = actualModifier,
            onClick = {
                onConfirm()
            }
        ) {
            Text(
                text = confirmButtonText,
                fontSize = textSize ?: TextUnit.Unspecified
            )
        }
    }
}