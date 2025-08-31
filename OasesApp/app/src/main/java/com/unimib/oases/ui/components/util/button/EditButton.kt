package com.unimib.oases.ui.components.util.button

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BoxScope.EditButton(
    onClick: () -> Unit,
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.align(Alignment.TopEnd)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = contentDescription
        )
    }
}