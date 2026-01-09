package com.unimib.oases.ui.components.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.PatientStatus
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.ui.components.card.VisitCard
import com.unimib.oases.ui.screen.nurse_assessment.history.VisitState

/**
 * A composable that represents a single node in a vertical timeline.
 * It draws a circle and lines to connect to previous and next nodes.
 *
 * @param modifier The modifier to be applied to the whole node.
 * @param dotRadius The radius of the circle representing the timeline event.
 * @param lineWidth The width of the vertical connecting lines.
 * @param lineColor The color of the connecting lines.
 * @param lineStyle The style of the line (e.g., solid, dashed).
 * @param isFirstNode If true, the top line will not be drawn.
 * @param isLastNode If true, the bottom line will not be drawn.
 * @param content The composable content to display next to the timeline node.
 */
@Composable
fun TimelineNode(
    modifier: Modifier = Modifier,
    dotRadius: Dp = 8.dp,
    lineWidth: Dp = 2.dp,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    lineStyle: TimelineNodeStyle = TimelineNodeStyle.Solid,
    isFirstNode: Boolean = false,
    isLastNode: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Row(modifier = modifier) {
        // The vertical line and dot are drawn on a Canvas
        Canvas(modifier = Modifier.fillMaxHeight()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val circleCenter = Offset(x = canvasWidth / 2, y = canvasHeight / 2)

            // Draw the top line only if it's not the first node
            if (!isFirstNode) {
                drawLine(
                    color = lineColor,
                    start = Offset(x = circleCenter.x, y = 0f),
                    end = Offset(x = circleCenter.x, y = circleCenter.y - dotRadius.toPx()),
                    strokeWidth = lineWidth.toPx(),
                    pathEffect = lineStyle.pathEffect
                )
            }

            // Draw the bottom line only if it's not the last node
            if (!isLastNode) {
                drawLine(
                    color = lineColor,
                    start = Offset(x = circleCenter.x, y = circleCenter.y + dotRadius.toPx()),
                    end = Offset(x = circleCenter.x, y = canvasHeight),
                    strokeWidth = lineWidth.toPx(),
                    pathEffect = lineStyle.pathEffect
                )
            }

            // Draw the center dot
            drawCircle(
                color = lineColor,
                radius = dotRadius.toPx(),
                center = circleCenter
            )
        }

        // The content is placed in a Box to the right of the timeline graphic
        Box(
            modifier = Modifier
                .padding(start = 24.dp) // Spacing between line and content
                .fillMaxHeight()
        ) {
            content()
        }
    }
}

/**
 * Defines the style of the connecting lines in a TimelineNode.
 */
enum class TimelineNodeStyle(val pathEffect: PathEffect?) {
    Solid(null),
    Dashed(PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
}

// =====================================================================================
// Preview and Example Usage
// =====================================================================================

@Preview(showBackground = true)
@Composable
private fun TimelinePreview() {
    // Example data for a "career" timeline
    val visits = listOf(
        VisitState(
            visitId = "1",
            date = "14/01/2023",
            triageCode = TriageCode.GREEN.getColor(),
            status = PatientStatus.WAITING_FOR_TRIAGE
        ),
        VisitState(
            visitId = "2",
            date = "14/05/2022",
            triageCode = TriageCode.RED.getColor(),
            status = PatientStatus.WAITING_FOR_TRIAGE
        ),
        VisitState(
            visitId = "3",
            date = "14/01/2022",
            triageCode = TriageCode.YELLOW.getColor(),
            status = PatientStatus.WAITING_FOR_TEST_RESULTS
        ),
        VisitState(
            visitId = "4",
            date = "14/01/2020",
            triageCode = TriageCode.GREEN.getColor(),
            status = PatientStatus.WAITING_FOR_VISIT
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        visits.forEachIndexed { index, visit ->
            TimelineNode(
                modifier = Modifier.height(IntrinsicSize.Min), // Ensures proper height calculation
                isFirstNode = index == 0,
                isLastNode = index == visits.size - 1
            ) {
                VisitCard(visit)
            }
        }
    }
}