package com.unimib.oases.ui.components.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AnimatedLabelOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    isInteger: Boolean = false,
    isDouble: Boolean = false,
    anchorModifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(anchorModifier)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (isInteger) {
                    onValueChange(it.filter { char -> char.isDigit() })
                } else if (isDouble){
                    val regex = Regex("^\\d*\\.?\\d{0,2}$")
                    if (it.isEmpty() || regex.matches(it)) {
                        onValueChange(it)
                    }
                } else {
                    onValueChange(it)
                }
            },
            label = { Text(labelText) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            keyboardOptions = when {
                isInteger || isDouble -> KeyboardOptions(keyboardType = KeyboardType.Number)
                else -> KeyboardOptions.Default
            },
            readOnly = readOnly,
            trailingIcon = trailingIcon
        )

        // Overlay a transparent clickable box on top of the entire text field
        if (readOnly) {
            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { if (onClick != null) onClick() }
            )
        }
    }
}