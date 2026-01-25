package com.unimib.oases.ui.components.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LabeledRadioButton(
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    asReversed: Boolean = false
){

    val radioButton = @Composable {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }

    val children = listOf(radioButton, label)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ){
        if (asReversed) {
            children.asReversed()
        }
        else {
            children
        }.forEach { it() }
    }
}