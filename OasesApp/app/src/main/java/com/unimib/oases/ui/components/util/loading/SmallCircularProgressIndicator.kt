package com.unimib.oases.ui.components.util.loading

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SmallCircularProgressIndicator(
    modifier: Modifier = Modifier
){
    CircularProgressIndicator(
        strokeWidth = 2.dp,
        modifier = modifier.size(16.dp)
    )
}