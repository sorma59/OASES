package com.unimib.oases.ui.components.util

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
    anchorModifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (isInteger) {
                onValueChange(it.filter { char -> char.isDigit() })
            } else
                onValueChange(it)
        },
        label = { Text(labelText) },
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .then(anchorModifier),
        keyboardOptions = if (isInteger || isDouble) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}