package com.unimib.oases.ui.screen.patient_registration.triage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.util.BottomButtons
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.ShowMoreArrow
import com.unimib.oases.ui.util.ToastUtils
import kotlinx.coroutines.launch

@Composable
fun RedCodeScreen(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
    onRedCodeToggle: (Boolean) -> Unit,
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
) {

    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var pregnancyRowScrollTargetY by remember { mutableFloatStateOf(0f) }

    val handlePregnancyChangeAndScroll = { newPregnancyState: Boolean ->
        onEvent(TriageEvent.FieldToggled("pregnancy"))
        if (newPregnancyState) { // Only scroll if it's being expanded
            coroutineScope.launch {
                scrollState.animateScrollTo(
                    value = pregnancyRowScrollTargetY.toInt(),
                    animationSpec = tween(
                        durationMillis = 500,
                        delayMillis = 100,
                        easing = LinearOutSlowInEasing
                    )
                )
            }
        }
    }

    LaunchedEffect(state.isRedCode) {
        onRedCodeToggle(state.isRedCode)
    }

    LaunchedEffect(state.toastMessage) {
        if (state.toastMessage != null) {
            ToastUtils.showToast(context, state.toastMessage)
            onEvent(TriageEvent.ToastShown)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
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
            Box {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    state.triageConfig!!.redOptions.forEach {
                        val id = it.id
                        ConditionalAnimatedVisibility(
                            applyWrapper = it.parent != null,
                            visible = state.selectedReds.contains(it.parent?.id),
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = if (it.isParent) Modifier.onGloballyPositioned { coordinates ->
                                        // This gives the Y position of the top of this Row
                                        // relative to the content area of the scrollable Column
                                        pregnancyRowScrollTargetY = coordinates.positionInParent().y
                                    } else Modifier
                                ) {
                                    LabeledCheckbox(
                                        label = it.label,
                                        checked = state.selectedReds.contains(it.id),
                                        onCheckedChange = { boolean: Boolean ->
                                            if (it.isParent)
                                                handlePregnancyChangeAndScroll(boolean)
                                            else
                                                onEvent(TriageEvent.FieldToggled(id))
                                        },
                                        modifier = if (it.parent != null) Modifier.padding(start = 16.dp) else Modifier
                                    )

                                    if (it.isParent) {
                                        ShowMoreArrow(
                                            expanded = state.selectedReds.contains(it.id),
                                            onClick = {
                                                handlePregnancyChangeAndScroll(it)
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    }

//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.onGloballyPositioned { coordinates ->
//                            // This gives the Y position of the top of this Row
//                            // relative to the content area of the scrollable Column
//                            pregnancyRowScrollTargetY = coordinates.positionInParent().y
//                        }
//                    ) {
//                        LabeledCheckbox(
//                            label = "Pregnancy with any of:",
//                            checked = state.pregnancy,
//                            onCheckedChange = {
//                                handlePregnancyChangeAndScroll(it)
//                            }
//                        )
//
//                        ShowMoreArrow(
//                            expanded = state.pregnancy,
//                            onClick = {
//                                handlePregnancyChangeAndScroll(it)
//                            }
//                        )
//                    }
//                    AnimatedVisibility(
//                        visible = state.pregnancy,
//                    ){
//                        Column(
//                            modifier = Modifier.padding(start = 16.dp)
//                        ) {
//                            LabeledCheckbox(
//                                label = "Heavy bleeding",
//                                checked = state.pregnancyWithHeavyBleeding,
//                                onCheckedChange = {
//                                    onEvent(
//                                        TriageEvent.PregnancyWithHeavyBleedingChanged(it)
//                                    )
//                                },
//                            )
//                        }
//                    }
                    Spacer(Modifier.height(48.dp))
                }
                FadeOverlay(Modifier.align(Alignment.BottomCenter))
            }
        }

        BottomButtons(
            onCancel = { onBack() },
            onConfirm = { onSubmitted() },
            cancelButtonText = "Back",
            confirmButtonText = "Next",
        )
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