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
fun NonRedCodeScreen(
    spo2Value: String,
    hrValue: String,
    rrValue: String,
    tempValue: String
) {
    var airwaySwellingMass by remember { mutableStateOf(false) }
    var ongoingBleeding by remember { mutableStateOf(false) }
    var severePallor by remember { mutableStateOf(false) }
    var ongoingSevereVomitingDiarrhea by remember { mutableStateOf(false) }
    var unableToFeedOrDrink by remember { mutableStateOf(false) }
    var recentFainting by remember { mutableStateOf(false) }
    var lethargyConfusionAgitation by remember { mutableStateOf(false) }
    var focalNeurologicVisualDeficit by remember { mutableStateOf(false) }
    var headacheWithStiffNeck by remember { mutableStateOf(false) }
    var severePain by remember { mutableStateOf(false) }
    var acuteTesticularScrotalPainPriapism by remember { mutableStateOf(false) }
    var unableToPassUrine by remember { mutableStateOf(false) }
    var acuteLimbDeformityOpenFracture by remember { mutableStateOf(false) }
    var otherTraumaBurns by remember { mutableStateOf(false) }
    var sexualAssault by remember { mutableStateOf(false) }
    var animalBiteNeedlestickPuncture by remember { mutableStateOf(false) }
    var otherPregnancyRelatedComplaints by remember { mutableStateOf(false) }
    var ageOver80Years by remember { mutableStateOf(false) }
    var alteredVitalSignsSpo2 by remember { mutableStateOf(false) }
    var alteredVitalSignsRrLow by remember { mutableStateOf(false) }
    var alteredVitalSignsRrHigh by remember { mutableStateOf(false) }
    var alteredVitalSignsHrLow by remember { mutableStateOf(false) }
    var alteredVitalSignsHrHigh by remember { mutableStateOf(false) }
    var alteredVitalSignsTempLow by remember { mutableStateOf(false) }
    var alteredVitalSignsTempHigh by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Calcoli dinamici per i segni vitali alterati
    val isSpo2LowCalculated = remember(spo2Value) { spo2Value.toIntOrNull()?.let { it < 92 } ?: false }
    val isRrLowCalculated = remember(rrValue) { rrValue.toIntOrNull()?.let { it < 10 } ?: false }
    val isRrHighCalculated = remember(rrValue) { rrValue.toIntOrNull()?.let { it > 30 } ?: false }
    val isHrLowCalculated = remember(hrValue) { hrValue.toIntOrNull()?.let { it < 50 } ?: false }
    val isHrHighCalculated = remember(hrValue) { hrValue.toIntOrNull()?.let { it > 130 } ?: false }
    val isTempLowCalculated = remember(tempValue) { tempValue.toDoubleOrNull()?.let { it < 35.0 } ?: false }
    val isTempHighCalculated = remember(tempValue) { tempValue.toDoubleOrNull()?.let { it > 39.0 } ?: false }

    // Resetta lo stato dei checkbox se i valori cambiano e non soddisfano la condizione
    LaunchedEffect(spo2Value) { alteredVitalSignsSpo2 = isSpo2LowCalculated }
    LaunchedEffect(rrValue) {
        alteredVitalSignsRrLow = isRrLowCalculated
        alteredVitalSignsRrHigh = isRrHighCalculated
    }
    LaunchedEffect(hrValue) {
        alteredVitalSignsHrLow = isHrLowCalculated
        alteredVitalSignsHrHigh = isHrHighCalculated
    }
    LaunchedEffect(tempValue) {
        alteredVitalSignsTempLow = isTempLowCalculated
        alteredVitalSignsTempHigh = isTempHighCalculated
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
                contentDescription = "YELLOW Code",
                tint = Color.Yellow,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "YELLOW Code",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            YellowCodeCheckbox(label = "Airway swelling/mass", checked = airwaySwellingMass, onCheckedChange = { airwaySwellingMass = it })
            YellowCodeCheckbox(label = "Ongoing bleeding (no red criteria)", checked = ongoingBleeding, onCheckedChange = { ongoingBleeding = it })
            YellowCodeCheckbox(label = "Severe pallor", checked = severePallor, onCheckedChange = { severePallor = it })
            YellowCodeCheckbox(label = "Ongoing severe vomiting/diarrhea", checked = ongoingSevereVomitingDiarrhea, onCheckedChange = { ongoingSevereVomitingDiarrhea = it })
            YellowCodeCheckbox(label = "Unable to feed or drink", checked = unableToFeedOrDrink, onCheckedChange = { unableToFeedOrDrink = it })
            YellowCodeCheckbox(label = "Recent fainting", checked = recentFainting, onCheckedChange = { recentFainting = it })
            YellowCodeCheckbox(label = "Lethargy/confusion/agitation", checked = lethargyConfusionAgitation, onCheckedChange = { lethargyConfusionAgitation = it })
            YellowCodeCheckbox(label = "Focal neurologic/visual deficit", checked = focalNeurologicVisualDeficit, onCheckedChange = { focalNeurologicVisualDeficit = it })
            YellowCodeCheckbox(label = "Headache with stiff neck", checked = headacheWithStiffNeck, onCheckedChange = { headacheWithStiffNeck = it })
            YellowCodeCheckbox(label = "Severe pain", checked = severePain, onCheckedChange = { severePain = it })
            YellowCodeCheckbox(label = "Acute testicular/scrotal pain/priapism", checked = acuteTesticularScrotalPainPriapism, onCheckedChange = { acuteTesticularScrotalPainPriapism = it })
            YellowCodeCheckbox(label = "Unable to pass urine", checked = unableToPassUrine, onCheckedChange = { unableToPassUrine = it })
            YellowCodeCheckbox(label = "Acute limb deformity/open fracture", checked = acuteLimbDeformityOpenFracture, onCheckedChange = { acuteLimbDeformityOpenFracture = it })
            YellowCodeCheckbox(label = "Other trauma/burns (no red criteria)", checked = otherTraumaBurns, onCheckedChange = { otherTraumaBurns = it })
            YellowCodeCheckbox(label = "Sexual assault", checked = sexualAssault, onCheckedChange = { sexualAssault = it })
            YellowCodeCheckbox(label = "Animal bite or needlestick puncture", checked = animalBiteNeedlestickPuncture, onCheckedChange = { animalBiteNeedlestickPuncture = it })
            YellowCodeCheckbox(label = "Other pregnancy-related complaints", checked = otherPregnancyRelatedComplaints, onCheckedChange = { otherPregnancyRelatedComplaints = it })
            YellowCodeCheckbox(label = "Age > 80 years", checked = ageOver80Years, onCheckedChange = { ageOver80Years = it })
            YellowCodeCheckbox(label = "SpO2 <92%", checked = alteredVitalSignsSpo2, onCheckedChange = { /* Non permettere la modifica manuale */ })
            YellowCodeCheckbox(label = "RR < 10", checked = alteredVitalSignsRrLow, onCheckedChange = { /* Non permettere la modifica manuale */ })
            YellowCodeCheckbox(label = "RR > 30", checked = alteredVitalSignsRrHigh, onCheckedChange = { /* Non permettere la modifica manuale */ })
            YellowCodeCheckbox(label = "HR <50", checked = alteredVitalSignsHrLow, onCheckedChange = { /* Non permettere la modifica manuale */ })
            YellowCodeCheckbox(label = "HR >130", checked = alteredVitalSignsHrHigh, onCheckedChange = { /* Non permettere la modifica manuale */ })
            YellowCodeCheckbox(label = "Temp <35°C", checked = alteredVitalSignsTempLow, onCheckedChange = { /* Non permettere la modifica manuale */ })
            YellowCodeCheckbox(label = "Temp >39°C", checked = alteredVitalSignsTempHigh, onCheckedChange = { /* Non permettere la modifica manuale */ })
        }
    }
}

@Composable
fun YellowCodeCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
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