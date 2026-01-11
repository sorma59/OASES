package com.unimib.oases.ui.components.util.loading

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.unimib.oases.R

/**
 * A full-screen overlay that blocks interactions and shows a branded loading indicator.
 * It uses the app's launcher icon inside a CircularProgressIndicator.
 */
@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
) {

    val imageSize = 64
    val imagePadding = 4
    val strokeWidth = 4
    val bufferingSize = imageSize + strokeWidth - imagePadding

    if (isLoading){
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                // Place this on top of the rest of the UI
                .zIndex(1f)
                // This blocks clicks from reaching the UI underneath
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(PointerEventPass.Initial)
                                .changes
                                .forEach { it.consume() }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // 1. The Circular Progress border
            CircularProgressIndicator(
                modifier = Modifier.size(bufferingSize.dp), // Slightly larger than the icon
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = strokeWidth.dp
            )

            // 2. The App Icon in the center
            Image(
                painter = painterResource(R.drawable.ic_launcher_round),
                contentDescription = "Loading",
                modifier = Modifier
                    .size(imageSize.dp)
                    .clip(CircleShape) // Ensures the icon stays round
                    .padding(imagePadding.dp) // Optional: small inset
            )
        }
    }
}