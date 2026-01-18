package com.unimib.oases.ui.components.util.logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unimib.oases.R

@Composable
fun OasesLogo(
    size: Int,
    padding: Int? = null
) {
    Image(
        painter = painterResource(R.drawable.ic_launcher_round),
        contentDescription = "Oases Logo",
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape) // Ensures the icon stays round
            .apply {
                padding?.let { padding ->
                    this.padding(padding.dp)
                }
            } // Optional: small inset
    )
}