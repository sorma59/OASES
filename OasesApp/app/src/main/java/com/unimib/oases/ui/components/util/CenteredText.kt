package com.unimib.oases.ui.components.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CenteredText(text: String, modifier: Modifier = Modifier){
    Box(modifier = Modifier.fillMaxWidth()){
        Text(text = text, textAlign = TextAlign.Center, modifier = modifier.padding(horizontal = 16.dp))
    }
}