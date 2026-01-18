package com.unimib.oases.ui.screen.nurse_assessment.triage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.symptom.TriageSymptom
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.ShowMoreArrow
import com.unimib.oases.ui.components.util.button.RetryButton
import kotlinx.coroutines.launch

@Composable
fun RedCodeContent(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
) {

    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    var pregnancyRowScrollTargetY by remember { mutableFloatStateOf(0f) }

    suspend fun animate() {
        scrollState.animateScrollTo(
            value = pregnancyRowScrollTargetY.toInt(),
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = 100,
                easing = LinearOutSlowInEasing
            )
        )
    }

    val handlePregnancyChangeAndScroll = { newPregnancyState: Boolean ->
        onEvent(TriageEvent.FieldToggled(TriageSymptom.PREGNANCY.id))
        if (newPregnancyState) { // Only scroll if it's being expanded
            coroutineScope.launch {
                animate()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "Red Code",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "RED Code",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        state.error?.let {
            RetryButton(
                error = state.error,
                onClick = { onEvent(TriageEvent.Retry) }
            )
        } ?: Box {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                state.editingState!!.triageConfig.redOptions.forEach {
                    val id = it.symptom.symptom.id
                    ConditionalAnimatedVisibility(
                        applyWrapper = it.symptom.parent != null,
                        visible = state.editingState.triageData.selectedReds.contains(it.symptom.parent?.symptom?.symptomId?.value?.string),
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = if (it.symptom.isParent) Modifier.onGloballyPositioned { coordinates ->
                                    // This gives the Y position of the top of this Row
                                    // relative to the content area of the scrollable Column
                                    pregnancyRowScrollTargetY = coordinates.positionInParent().y
                                } else Modifier
                            ) {
                                LabeledCheckbox(
                                    label = it.label,
                                    checked = state.editingState.triageData.selectedReds.contains(id),
                                    onCheckedChange = { boolean: Boolean ->
                                        if (it.symptom.isParent)
                                            handlePregnancyChangeAndScroll(boolean)
                                        else
                                            onEvent(TriageEvent.FieldToggled(id))
                                    },
                                    modifier = if (it.symptom.parent != null) Modifier.padding(
                                        start = 16.dp
                                    ) else Modifier
                                )

                                if (it.symptom.id == TriageSymptom.PREGNANCY.id
                                    && it.symptom.isParent
                                ) {
                                    ShowMoreArrow(
                                        expanded = state.editingState.triageData.selectedReds.contains(id),
                                        onClick = {  value ->
                                            handlePregnancyChangeAndScroll(value)
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
                Spacer(Modifier.height(48.dp))
            }
            FadeOverlay(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun ConditionalAnimatedVisibility(
    applyWrapper: Boolean,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    if (applyWrapper) {
        // Apply the wrapper
        AnimatedVisibility(
            visible = visible,
        ){
            content() // Place the original content inside the wrapper
        }
    } else {
        // Don't apply the wrapper, just render the content
        content()
    }
}