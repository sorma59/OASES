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
        onEvent(TriageEvent.PregnancyChanged(newPregnancyState))
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
                    LabeledCheckbox(
                        label = "Unconsciousness",
                        checked = state.unconsciousness,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.UnconsciousnessChanged(it)
                            )
                        }
                    )
                    LabeledCheckbox(
                        label = "Active convulsions",
                        checked = state.activeConvulsions,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.ActiveConvulsionsChanged(it)
                            )
                        })
                    LabeledCheckbox(
                        label = "Respiratory distress",
                        checked = state.respiratoryDistress,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.RespiratoryDistressChanged(it)
                            )
                        })
                    LabeledCheckbox(
                        label = "Heavy bleeding",
                        checked = state.heavyBleeding,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.HeavyBleedingChanged(it)
                            )
                        }
                    )
                    LabeledCheckbox(
                        label = "High-risk trauma/burns",
                        checked = state.highRiskTraumaBurns,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.HighRiskTraumaBurnsChanged(it)
                            )
                        }
                    )
                    LabeledCheckbox(
                        label = "Threatened limb",
                        checked = state.threatenedLimb,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.ThreatenedLimbChanged(it)
                            )
                        }
                    )
                    LabeledCheckbox(
                        label = "Poisoning/intoxication",
                        checked = state.poisoningIntoxication,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.PoisoningIntoxicationChanged(it)
                            )
                        }
                    )
                    LabeledCheckbox(
                        label = "Snake bite",
                        checked = state.snakeBite,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.SnakeBiteChanged(it)
                            )
                        }
                    )
                    LabeledCheckbox(
                        label = "Aggressive behavior",
                        checked = state.aggressiveBehavior,
                        onCheckedChange = {
                            onEvent(
                                TriageEvent.AggressiveBehaviorChanged(it)
                            )
                        }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            // This gives the Y position of the top of this Row
                            // relative to the content area of the scrollable Column
                            pregnancyRowScrollTargetY = coordinates.positionInParent().y
                        }
                    ) {
                        LabeledCheckbox(
                            label = "Pregnancy with any of:",
                            checked = state.pregnancy,
                            onCheckedChange = {
                                handlePregnancyChangeAndScroll(it)
                            }
                        )

                        ShowMoreArrow(
                            expanded = state.pregnancy,
                            onClick = {
                                handlePregnancyChangeAndScroll(it)
                            }
                        )
                    }
                    AnimatedVisibility(
                        visible = state.pregnancy,
                    ){
                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            LabeledCheckbox(
                                label = "Heavy bleeding",
                                checked = state.pregnancyWithHeavyBleeding,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithHeavyBleedingChanged(it)
                                    )
                                },
                            )
                            LabeledCheckbox(
                                label = "Severe abdominal pain",
                                checked = state.pregnancyWithSevereAbdominalPain,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithSevereAbdominalPainChanged(it)
                                    )
                                }
                            )
                            LabeledCheckbox(
                                label = "Seizures",
                                checked = state.pregnancyWithSeizures,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithSeizuresChanged(it)
                                    )
                                }
                            )
                            LabeledCheckbox(
                                label = "Altered mental status",
                                checked = state.pregnancyWithAlteredMentalStatus,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithAlteredMentalStatusChanged(it)
                                    )
                                }
                            )
                            LabeledCheckbox(
                                label = "Severe headache",
                                checked = state.pregnancyWithSevereHeadache,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithSevereHeadacheChanged(it)
                                    )
                                }
                            )
                            LabeledCheckbox(
                                label = "Visual changes",
                                checked = state.pregnancyWithVisualChanges,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithVisualChangesChanged(it)
                                    )
                                }
                            )
                            LabeledCheckbox(
                                label = "SBP ≥ ${TriageState.PREGNANCY_HIGH_SBP} or DBP ≥ ${TriageState.PREGNANCY_HIGH_DBP} (computed)",
                                checked = state.sbpHighDbpHighForPregnancy,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.ComputedFieldClicked
                                    )
                                }
                            )
                            LabeledCheckbox(
                                label = "Trauma",
                                checked = state.pregnancyWithTrauma,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithTraumaChanged(it)
                                    )
                                })
                            LabeledCheckbox(
                                label = "Active labor",
                                checked = state.pregnancyWithActiveLabor,
                                onCheckedChange = {
                                    onEvent(
                                        TriageEvent.PregnancyWithActiveLaborChanged(it)
                                    )
                                }
                            )
                        }
                    }
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