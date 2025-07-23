package com.unimib.oases.ui.screen.patient_registration.triage

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.util.BottomButtons
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.ShowMoreArrow

@Composable
fun RedCodeScreen(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
    onRedCodeToggle: (Boolean) -> Unit,
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
) {

    val scrollState = rememberScrollState()

    var showPregnancyRelatedSymptoms by remember { mutableStateOf(false) }

    LaunchedEffect(state.isRedCode) {
        onRedCodeToggle(state.isRedCode)
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
            Box{
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

                    ShowMoreArrow(
                        expanded = showPregnancyRelatedSymptoms,
                        onClick = { showPregnancyRelatedSymptoms = !showPregnancyRelatedSymptoms },
                        expandedLabel = "Pregnancy",
                        collapsedLabel = "Pregnancy",
                        modifier = Modifier.padding(start = 12.dp)
                    )

                    if (showPregnancyRelatedSymptoms){
                        LabeledCheckbox(
                            label = "Pregnancy with heavy bleeding",
                            checked = state.pregnancyWithHeavyBleeding,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithHeavyBleedingChanged(it)
                                )
                            },
                        )
                        LabeledCheckbox(
                            label = "Pregnancy with severe abdominal pain",
                            checked = state.pregnancyWithSevereAbdominalPain,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithSevereAbdominalPainChanged(it)
                                )
                            }
                        )
                        LabeledCheckbox(
                            label = "Pregnancy with seizures",
                            checked = state.pregnancyWithSeizures,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithSeizuresChanged(it)
                                )
                            }
                        )
                        LabeledCheckbox(
                            label = "Pregnancy with altered mental status",
                            checked = state.pregnancyWithAlteredMentalStatus,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithAlteredMentalStatusChanged(it)
                                )
                            }
                        )
                        LabeledCheckbox(
                            label = "Pregnancy with severe headache",
                            checked = state.pregnancyWithSevereHeadache,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithSevereHeadacheChanged(it)
                                )
                            }
                        )
                        LabeledCheckbox(
                            label = "Pregnancy with visual changes",
                            checked = state.pregnancyWithVisualChanges,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithVisualChangesChanged(it)
                                )
                            }
                        )
                        LabeledCheckbox(
                            label = "Pregnancy with SBP ≥ ${TriageState.PREGNANCY_HIGH_SBP} or DBP ≥ ${TriageState.PREGNANCY_HIGH_DBP}",
                            checked = state.pregnancyWithSbpHighDpbHigh,
                            onCheckedChange = { }
                        )
                        LabeledCheckbox(
                            label = "Pregnancy with trauma",
                            checked = state.pregnancyWithTrauma,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithTraumaChanged(it)
                                )
                            })
                        LabeledCheckbox(
                            label = "Pregnancy with active labor",
                            checked = state.pregnancyWithActiveLabor,
                            onCheckedChange = {
                                onEvent(
                                    TriageEvent.PregnancyWithActiveLaborChanged(it)
                                )
                            }
                        )
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