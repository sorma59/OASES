package com.unimib.oases.ui.components.util

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 22,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = Color.Unspecified,
){
    Text(text = text, fontSize = fontSize.sp, fontWeight = fontWeight, color = color, modifier = modifier)
}
