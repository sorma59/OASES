package com.unimib.oases.ui.screen.patient_registration.triage.non_red_code

import androidx.compose.foundation.layout.Arrangement
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
import com.unimib.oases.ui.components.util.TitleText

@Composable
fun NonRedCodeScreen(
    onYellowCodeSelected: (Boolean) -> Unit,
    ageInt: Int,
    spo2Value: String,
    hrValue: String,
    rrValue: String,
    sbpValue: String,
    tempValue: String
) {
    val nonRedCodeViewModel: NonRedCodeViewModel = hiltViewModel()

    nonRedCodeViewModel.updateVitalSignsAndAge(ageInt, spo2Value, rrValue, hrValue, sbpValue, tempValue)

    val state by nonRedCodeViewModel.state.collectAsState()

    val scrollState = rememberScrollState()

    val isAnyYellowCodeSelected = remember {
        derivedStateOf {
            state.airwaySwellingMass || state.ongoingBleeding || state.severePallor || state.ongoingSevereVomitingDiarrhea ||
            state.unableToFeedOrDrink || state.recentFainting || state.lethargyConfusionAgitation || state.focalNeurologicVisualDeficit ||
            state.headacheWithStiffNeck || state.severePain || state.acuteTesticularScrotalPainPriapism || state.unableToPassUrine ||
            state.acuteLimbDeformityOpenFracture || state.otherTraumaBurns || state.sexualAssault || state.animalBiteNeedlestickPuncture ||
            state.otherPregnancyRelatedComplaints || state.ageOver80Years || state.alteredVitalSignsSpo2 || state.alteredVitalSignsRrLow ||
            state.alteredVitalSignsRrHigh || state.alteredVitalSignsHrLow || state.alteredVitalSignsHrHigh || state.alteredVitalSignsTempLow ||
            state.alteredVitalSignsTempHigh
        }
    }

    LaunchedEffect(isAnyYellowCodeSelected.value) {
        if(isAnyYellowCodeSelected.value) onYellowCodeSelected(isAnyYellowCodeSelected.value)
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
            YellowCodeCheckbox(
                label = "Airway swelling/mass",
                checked = state.airwaySwellingMass,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.AirwaySwellingMassChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Ongoing bleeding (no red criteria)",
                checked = state.ongoingBleeding,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.OngoingBleedingChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Severe pallor",
                checked = state.severePallor,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.SeverePallorChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Ongoing severe vomiting/diarrhea",
                checked = state.ongoingSevereVomitingDiarrhea,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.OngoingSevereVomitingDiarrheaChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Unable to feed or drink",
                checked = state.unableToFeedOrDrink,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.UnableToFeedOrDrinkChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Recent fainting",
                checked = state.recentFainting,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.RecentFaintingChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Lethargy/confusion/agitation",
                checked = state.lethargyConfusionAgitation,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.LethargyConfusionAgitationChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Focal neurologic/visual deficit",
                checked = state.focalNeurologicVisualDeficit,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.FocalNeurologicalVisualDeficitChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Headache with stiff neck",
                checked = state.headacheWithStiffNeck,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.HeadacheWithStiffNeckChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Severe pain",
                checked = state.severePain,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.SeverePainChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Acute testicular/scrotal pain/priapism",
                checked = state.acuteTesticularScrotalPainPriapism,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.AcuteTesticularScrotalPainPriapismChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Unable to pass urine",
                checked = state.unableToPassUrine,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.UnableToPassUrineChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Acute limb deformity/open fracture",
                checked = state.acuteLimbDeformityOpenFracture,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.AcuteLimbDeformityOpenFractureChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Other trauma/burns (no red criteria)",
                checked = state.otherTraumaBurns,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.OtherTraumaBurnsChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Sexual assault",
                checked = state.sexualAssault,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.SexualAssaultChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Animal bite or needlestick puncture",
                checked = state.animalBiteNeedlestickPuncture,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.AnimalBiteNeedlestickPunctureChanged(it)
                    )
                }
            )
            YellowCodeCheckbox(
                label = "Other pregnancy-related complaints",
                checked = state.otherPregnancyRelatedComplaints,
                onCheckedChange = {
                    nonRedCodeViewModel.onEvent(
                        NonRedCodeEvent.OtherPregnancyRelatedComplaintsChanged(it)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TitleText("Vital Signs and Age")

            YellowCodeCheckbox(
                label = "Age > 80 years",
                checked = state.ageOver80Years,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "SpO2 < 92%",
                checked = state.alteredVitalSignsSpo2,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "RR < 10",
                checked = state.alteredVitalSignsRrLow,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "RR > 30",
                checked = state.alteredVitalSignsRrHigh,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "HR < 50",
                checked = state.alteredVitalSignsHrLow,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "HR > 130",
                checked = state.alteredVitalSignsHrHigh,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "SBP < 90",
                checked = state.alteredVitalSignsSbpLow,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "SBP > 200",
                checked = state.alteredVitalSignsSbpHigh,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "Temp < 35°C",
                checked = state.alteredVitalSignsTempLow,
                onCheckedChange = { }
            )
            YellowCodeCheckbox(
                label = "Temp > 39°C",
                checked = state.alteredVitalSignsTempHigh,
                onCheckedChange = { }
            )
        }
    }
}

@Composable
fun YellowCodeCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
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