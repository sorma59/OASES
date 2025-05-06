package com.unimib.oases.ui.screen.patient_registration.triage

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TriageScreen(onRedCodeSelected: (Boolean) -> Unit, sbpValue: String, dbpValue: String) {
    var unconsciousness by remember { mutableStateOf(false) }
    var activeConvulsions by remember { mutableStateOf(false) }
    var respiratoryDistress by remember { mutableStateOf(false) }
    var heavyBleeding by remember { mutableStateOf(false) }
    var highRiskTraumaBurns by remember { mutableStateOf(false) }
    var threatenedLimb by remember { mutableStateOf(false) }
    var poisoningIntoxication by remember { mutableStateOf(false) }
    var snakeBite by remember { mutableStateOf(false) }
    var aggressiveBehavior by remember { mutableStateOf(false) }
    var pregnancyHeavyBleeding by remember { mutableStateOf(false) }
    var severeAbdominalPain by remember { mutableStateOf(false) }
    var seizures by remember { mutableStateOf(false) }
    var alteredMentalStatus by remember { mutableStateOf(false) }
    var severeHeadache by remember { mutableStateOf(false) }
    var visualChanges by remember { mutableStateOf(false) }
    var sbpHighDpbHigh by remember { mutableStateOf(false) }
    var trauma by remember { mutableStateOf(false) }
    var activeLabor by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val isSbpHighDpbHighCalculated = remember(sbpValue, dbpValue) {
        val sbp = sbpValue.toIntOrNull() ?: 0
        val dbp = dbpValue.toIntOrNull() ?: 0
        sbp >= 160 || dbp >= 110
    }

    // Resetta lo stato sbpHighDpbHigh se i valori cambiano e non soddisfano la condizione
    LaunchedEffect(sbpValue, dbpValue) {
        if (!isSbpHighDpbHighCalculated) {
            sbpHighDpbHigh = false
        } else {
            sbpHighDpbHigh = true
        }
    }

    val isAnyRedCodeSelected = remember {
        derivedStateOf {
            unconsciousness || activeConvulsions || respiratoryDistress || heavyBleeding ||
                    highRiskTraumaBurns || threatenedLimb || poisoningIntoxication || snakeBite ||
                    aggressiveBehavior || pregnancyHeavyBleeding || severeAbdominalPain || seizures ||
                    alteredMentalStatus || severeHeadache || visualChanges || sbpHighDpbHigh ||
                    trauma || activeLabor
        }
    }

    LaunchedEffect(isAnyRedCodeSelected.value) {
        onRedCodeSelected(isAnyRedCodeSelected.value)
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
            RedCodeCheckbox(label = "Unconsciousness", checked = unconsciousness, onCheckedChange = { unconsciousness = it })
            RedCodeCheckbox(label = "Active convulsions", checked = activeConvulsions, onCheckedChange = { activeConvulsions = it })
            RedCodeCheckbox(label = "Respiratory distress", checked = respiratoryDistress, onCheckedChange = { respiratoryDistress = it })
            RedCodeCheckbox(label = "Heavy bleeding", checked = heavyBleeding, onCheckedChange = { heavyBleeding = it })
            RedCodeCheckbox(label = "High-risk trauma/burns", checked = highRiskTraumaBurns, onCheckedChange = { highRiskTraumaBurns = it })
            RedCodeCheckbox(label = "Threatened limb", checked = threatenedLimb, onCheckedChange = { threatenedLimb = it })
            RedCodeCheckbox(label = "Poisoning/intoxication", checked = poisoningIntoxication, onCheckedChange = { poisoningIntoxication = it })
            RedCodeCheckbox(label = "Snake bite", checked = snakeBite, onCheckedChange = { snakeBite = it })
            RedCodeCheckbox(label = "Aggressive behavior", checked = aggressiveBehavior, onCheckedChange = { aggressiveBehavior = it })
            RedCodeCheckbox(label = "Pregnancy with any of Heavy bleeding", checked = pregnancyHeavyBleeding, onCheckedChange = { pregnancyHeavyBleeding = it })
            RedCodeCheckbox(label = "Severe abdominal pain", checked = severeAbdominalPain, onCheckedChange = { severeAbdominalPain = it })
            RedCodeCheckbox(label = "Seizures", checked = seizures, onCheckedChange = { seizures = it })
            RedCodeCheckbox(label = "Altered mental status", checked = alteredMentalStatus, onCheckedChange = { alteredMentalStatus = it })
            RedCodeCheckbox(label = "Severe headache", checked = severeHeadache, onCheckedChange = { severeHeadache = it })
            RedCodeCheckbox(label = "Visual changes", checked = visualChanges, onCheckedChange = { visualChanges = it })
            RedCodeCheckbox(
                label = "SBP ≥160 or DBP ≥110",
                checked = sbpHighDpbHigh,
                onCheckedChange = { /* Non permettere la modifica manuale, è basato sui vital signs */ }
            )
            RedCodeCheckbox(label = "Trauma", checked = trauma, onCheckedChange = { trauma = it })
            RedCodeCheckbox(label = "Active labor", checked = activeLabor, onCheckedChange = { activeLabor = it })
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