package com.unimib.oases.ui.components.util.circularprogressindicator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            .clickable(onClick = {})
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ){
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun CustomCircularProgressIndicatorPreview() {
    CustomCircularProgressIndicator()
}