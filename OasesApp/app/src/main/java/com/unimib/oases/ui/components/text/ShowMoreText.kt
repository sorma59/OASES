package com.unimib.oases.ui.components.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.unimib.oases.ui.components.util.ShowMoreArrow

@Composable
fun ShowMoreText(
    label: String
){

    var maxLines by remember { mutableStateOf(MaxLines.ONE) }

    val onClick = { expanded: Boolean ->
        maxLines = if (maxLines.expanded)
            MaxLines.ONE
        else
            MaxLines.MAX
    }

    val modifier =
        if (!maxLines.expanded)
            Modifier
                .graphicsLayer(alpha = 0.99f) // needed for BlendMode
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black, // fully visible
                                Color.Transparent // fade out
                            ),
                            startX = size.width * (1 - 0.4f),
                            endX = size.width
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        else
            Modifier

    Row(
        Modifier.clickable(
            onClick = { onClick(!maxLines.expanded) }
        )
    ) {
        Text(
            text = label,
            modifier = modifier.weight(1f),
            maxLines = maxLines.lines
        )

        ShowMoreArrow(
            expanded = maxLines.expanded,
            onClick = { onClick(!maxLines.expanded) }
        )
    }
}

enum class MaxLines(val lines: Int, val expanded: Boolean) {
    ONE(1, false),
    MAX(Int.MAX_VALUE, true)
}