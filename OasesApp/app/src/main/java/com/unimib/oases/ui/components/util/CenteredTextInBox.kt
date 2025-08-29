package com.unimib.oases.ui.components.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CenteredTextInBox(
    text: String,
    color: Color = Color.Unspecified
){
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = color
        )
    }
}