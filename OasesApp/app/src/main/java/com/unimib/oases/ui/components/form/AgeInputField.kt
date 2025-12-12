package com.unimib.oases.ui.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AgeInputField(
    ageInMonths: Int?,
    onAgeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var isMonths by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(ageInMonths, isMonths) {
        inputText = updateInputText(ageInMonths, isMonths)
    }

    LaunchedEffect(isFocused) {
        if (!isFocused)
            inputText = updateInputText(ageInMonths, isMonths)
    }

    OutlinedTextField(
        value = inputText,
        onValueChange = { newAge ->
            if (newAge.length <= 3 && newAge.all { it.isDigit() }){ // Update only if all characters are digits
                inputText = newAge
                val raw = newAge.toIntOrNull()
                if (raw != null) {
                    onAgeChange(if (isMonths) raw else raw * 12)
                }
            }
        },
        label = { Text(if (isMonths) "Age (months)" else "Age (years)") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        isError = isError,
        trailingIcon = {
            TextButton(
                onClick = { isMonths = !isMonths }
            ) {
                Text( if (isMonths) "Switch to years" else "Infant?" )
            }
        }
    )
}

private fun updateInputText(ageInMonths: Int?, isMonths: Boolean): String {
    return ageInMonths?.let {
        if (isMonths) it.toString() else (it / 12).toString()
    } ?: ""
}