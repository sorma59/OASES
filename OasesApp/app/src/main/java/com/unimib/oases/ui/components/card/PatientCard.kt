@file:JvmName("PatientCardKt")

package com.unimib.oases.ui.components.card

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.domain.model.TriageCode
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PatientCard(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    patient: Patient,
    onCardClick: () -> Unit,
) {
    var contextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    val offset = remember {
        Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isRevealed, contextMenuWidth) {
        if (isRevealed) {
            offset.animateTo(contextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(

        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .height(IntrinsicSize.Min)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .onSizeChanged {
                    contextMenuWidth = it.width.toFloat()
                }
                .background(color = MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            actions()
        }
        Card(
            onClick = onCardClick,
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(0f, contextMenuWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= contextMenuWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                },
            colors = CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.primary),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    text = patient.name + ", " + patient.age,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = patient.publicId,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "State:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.surface,
                            fontWeight = FontWeight.Normal,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = "",
                            tint = when (patient.status) {
                                TriageCode.YELLOW.name -> Color.Yellow
                                TriageCode.RED.name -> Color.Red
                                TriageCode.GREEN.name -> Color.Green
                                else -> Color.Gray
                            },
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
        }
    }
}