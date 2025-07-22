package com.unimib.oases.ui.screen.patient_registration.triage

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.TitleText

@Composable
fun YellowCodeScreen(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
    onYellowCodeToggle: (Boolean) -> Unit,
) {

    val scrollState = rememberScrollState()

    LaunchedEffect(state.isYellowCode) {
        onYellowCodeToggle(state.isYellowCode)
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
                contentDescription = "Yellow Code",
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
        Box{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                LabeledCheckbox(
                    label = "Airway swelling/mass",
                    checked = state.airwaySwellingMass,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.AirwaySwellingMassChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Ongoing bleeding (no red criteria)",
                    checked = state.ongoingBleeding,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.OngoingBleedingChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Severe pallor",
                    checked = state.severePallor,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.SeverePallorChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Ongoing severe vomiting/diarrhea",
                    checked = state.ongoingSevereVomitingDiarrhea,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.OngoingSevereVomitingDiarrheaChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Unable to feed or drink",
                    checked = state.unableToFeedOrDrink,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.UnableToFeedOrDrinkChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Recent fainting",
                    checked = state.recentFainting,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.RecentFaintingChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Lethargy/confusion/agitation",
                    checked = state.lethargyConfusionAgitation,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.LethargyConfusionAgitationChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Focal neurologic/visual deficit",
                    checked = state.focalNeurologicVisualDeficit,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.FocalNeurologicVisualDeficitChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Headache with stiff neck",
                    checked = state.headacheWithStiffNeck,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.HeadacheWithStiffNeckChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Severe pain",
                    checked = state.severePain,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.SeverePainChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Acute testicular/scrotal pain/priapism",
                    checked = state.acuteTesticularScrotalPainPriapism,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.AcuteTesticularScrotalPainPriapismChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Unable to pass urine",
                    checked = state.unableToPassUrine,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.UnableToPassUrineChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Acute limb deformity/open fracture",
                    checked = state.acuteLimbDeformityOpenFracture,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.AcuteLimbDeformityOpenFractureChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Other trauma/burns (no red criteria)",
                    checked = state.otherTraumaBurns,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.OtherTraumaBurnsChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Sexual assault",
                    checked = state.sexualAssault,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.SexualAssaultChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Animal bite or needlestick puncture",
                    checked = state.animalBiteNeedlestickPuncture,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.AnimalBiteNeedlestickPunctureChanged(it)
                        )
                    }
                )
                LabeledCheckbox(
                    label = "Other pregnancy-related complaints",
                    checked = state.otherPregnancyRelatedComplaints,
                    onCheckedChange = {
                        onEvent(
                            TriageEvent.OtherPregnancyRelatedComplaintsChanged(it)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TitleText("Vital Signs and Age")

                LabeledCheckbox(
                    label = "Age > 80 years",
                    checked = state.ageOver80Years,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "SpO2 < 92%",
                    checked = state.alteredVitalSignsSpo2,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "RR < 10",
                    checked = state.alteredVitalSignsRrLow,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "RR > 30",
                    checked = state.alteredVitalSignsRrHigh,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "HR < 50",
                    checked = state.alteredVitalSignsHrLow,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "HR > 130",
                    checked = state.alteredVitalSignsHrHigh,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "SBP < 90",
                    checked = state.alteredVitalSignsSbpLow,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "SBP > 200",
                    checked = state.alteredVitalSignsSbpHigh,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "Temp < 35°C",
                    checked = state.alteredVitalSignsTempLow,
                    onCheckedChange = { }
                )
                LabeledCheckbox(
                    label = "Temp > 39°C",
                    checked = state.alteredVitalSignsTempHigh,
                    onCheckedChange = { }
                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            FadeOverlay(Modifier.align(Alignment.BottomCenter))
        }
    }
}