package com.unimib.oases.ui.components.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun ShowMoreArrow(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expandedLabel: String = "Show less",
    collapsedLabel: String = "Show more",
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Arrow Rotation"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = if (expanded) expandedLabel else collapsedLabel,
//            color = androidx.compose.material3.MaterialTheme.colorScheme.outline
        )

        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = if (expanded) expandedLabel else collapsedLabel,
            modifier = Modifier.rotate(rotationAngle),
//            tint = androidx.compose.material3.MaterialTheme.colorScheme.outline
        )
    }
}