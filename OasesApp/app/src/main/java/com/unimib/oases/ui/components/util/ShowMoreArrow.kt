package com.unimib.oases.ui.components.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun ShowMoreArrow(
    expanded: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Arrow Rotation"
    )

    var isExpanded by remember { mutableStateOf(expanded) }

    IconButton(
        onClick = {
            isExpanded = !isExpanded
            onClick(isExpanded)
        },
        modifier = modifier
    ){
        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = "show more icon button",
            modifier = Modifier.rotate(rotationAngle),
        )
    }
}