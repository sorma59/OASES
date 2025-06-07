package com.unimib.oases.ui.screen.patient_registration.triage.red_code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RedCodeScreen(onRedCodeSelected: (Boolean) -> Unit, sbpValue: String, dbpValue: String) {

    val redCodeViewModel: RedCodeViewModel = hiltViewModel()

    redCodeViewModel.updateVitalSigns(sbpValue, dbpValue)

    val state by redCodeViewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    val isAnyRedCodeSelected = remember {
        derivedStateOf {
            state.unconsciousness || state.activeConvulsions || state.respiratoryDistress || state.heavyBleeding ||
            state.highRiskTraumaBurns || state.threatenedLimb || state.poisoningIntoxication || state.snakeBite ||
            state.aggressiveBehavior || state.pregnancyHeavyBleeding || state.severeAbdominalPain || state.seizures ||
            state.alteredMentalStatus || state.severeHeadache || state.visualChanges || state.sbpHighDpbHigh ||
            state.trauma || state.activeLabor
        }
    }

    LaunchedEffect(isAnyRedCodeSelected.value) {
      if(isAnyRedCodeSelected.value) onRedCodeSelected(isAnyRedCodeSelected.value)
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
                contentDescription = "RED Code",
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            RedCodeCheckbox(
                label = "Unconsciousness",
                checked = state.unconsciousness,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.UnconsciousnessChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Active convulsions",
                checked = state.activeConvulsions,
                onCheckedChange = { redCodeViewModel.onEvent(
                    RedCodeEvent.ActiveConvulsionsChanged(it)
                ) })
            RedCodeCheckbox(
                label = "Respiratory distress",
                checked = state.respiratoryDistress,
                onCheckedChange = { redCodeViewModel.onEvent(
                    RedCodeEvent.RespiratoryDistressChanged(it)
                ) })
            RedCodeCheckbox(
                label = "Heavy bleeding",
                checked = state.heavyBleeding,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.HeavyBleedingChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "High-risk trauma/burns",
                checked = state.highRiskTraumaBurns,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.HighRiskTraumaBurnsChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Threatened limb",
                checked = state.threatenedLimb,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.ThreatenedLimbChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Poisoning/intoxication",
                checked = state.poisoningIntoxication,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.PoisoningIntoxicationChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Snake bite",
                checked = state.snakeBite,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.SnakeBiteChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Aggressive behavior",
                checked = state.aggressiveBehavior,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.AggressiveBehaviorChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Pregnancy with any of Heavy bleeding",
                checked = state.pregnancyHeavyBleeding,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.PregnancyHeavyBleedingChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Severe abdominal pain",
                checked = state.severeAbdominalPain,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.SevereAbdominalPainChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Seizures",
                checked = state.seizures,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.SeizuresChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Altered mental status",
                checked = state.alteredMentalStatus,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.AlteredMentalStatusChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Severe headache",
                checked = state.severeHeadache,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.SevereHeadacheChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "Visual changes",
                checked = state.visualChanges,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.VisualChangesChanged(it)
                    )
                }
            )
            RedCodeCheckbox(
                label = "SBP ≥160 or DBP ≥110",
                checked = state.sbpHighDpbHigh,
                onCheckedChange = {  }
            )
            RedCodeCheckbox(
                label = "Trauma",
                checked = state.trauma,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.TraumaChanged(it)
                    )
                })
            RedCodeCheckbox(
                label = "Active labor",
                checked = state.activeLabor,
                onCheckedChange = {
                    redCodeViewModel.onEvent(
                        RedCodeEvent.ActiveLaborChanged(it)
                    )
                }
            )
        }
    }
}

@Composable
fun RedCodeCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}