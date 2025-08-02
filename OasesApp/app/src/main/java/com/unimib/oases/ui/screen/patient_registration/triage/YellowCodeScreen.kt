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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.input.LabeledCheckbox
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.util.ToastUtils

@Composable
fun YellowCodeScreen(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
    onYellowCodeToggle: (Boolean) -> Unit,
) {

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    LaunchedEffect(state.isYellowCode) {
        onYellowCodeToggle(state.isYellowCode)
    }

    LaunchedEffect(state.toastMessage) {
        if (state.toastMessage != null) {
            ToastUtils.showToast(context, state.toastMessage)
            onEvent(TriageEvent.ToastShown)
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {

                state.triageConfig!!.yellowOptions.forEach {
                    val id = it.id
                    LabeledCheckbox(
                        label = it.label,
                        checked = state.selectedYellows.contains(it.id),
                        onCheckedChange = { onEvent(TriageEvent.FieldToggled(id)) }
                    )
                }

//                LabeledCheckbox(
//                    label = "Airway swelling/mass",
//                    checked = state.airwaySwellingOrAirwayMass,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.AirwaySwellingMassChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Ongoing bleeding (no red criteria)",
//                    checked = state.ongoingBleeding,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.OngoingBleedingChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Severe pallor",
//                    checked = state.severePallor,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.SeverePallorChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Ongoing severe vomiting/diarrhea",
//                    checked = state.ongoingSevereVomitingOrOngoingSevereDiarrhea,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.OngoingSevereVomitingDiarrheaChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Unable to feed or drink",
//                    checked = state.unableToFeedOrDrink,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.UnableToFeedOrDrinkChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Recent fainting",
//                    checked = state.recentFainting,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.RecentFaintingChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Lethargy/confusion/agitation",
//                    checked = state.lethargyOrConfusionOrAgitation,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.LethargyConfusionAgitationChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Focal neurologic/visual deficit",
//                    checked = state.focalNeurologicDeficitOrFocalVisualDeficit,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.FocalNeurologicVisualDeficitChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Headache with stiff neck",
//                    checked = state.headacheWithStiffNeck,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.HeadacheWithStiffNeckChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Severe pain",
//                    checked = state.severePain,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.SeverePainChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Acute testicular/scrotal pain/priapism",
//                    checked = state.acuteTesticularOrScrotalPainOrPriapism,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.AcuteTesticularScrotalPainPriapismChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Unable to pass urine",
//                    checked = state.unableToPassUrine,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.UnableToPassUrineChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Acute limb deformity/open fracture",
//                    checked = state.acuteLimbDeformityOrOpenFracture,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.AcuteLimbDeformityOpenFractureChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Other trauma/burns (no red criteria)",
//                    checked = state.otherTraumaOrOtherBurns,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.OtherTraumaBurnsChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Sexual assault",
//                    checked = state.sexualAssault,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.SexualAssaultChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Animal bite or needlestick puncture",
//                    checked = state.animalBiteOrNeedlestickPuncture,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.AnimalBiteNeedlestickPunctureChanged(it)
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Other pregnancy-related complaints",
//                    checked = state.otherPregnancyRelatedComplaints,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.OtherPregnancyRelatedComplaintsChanged(it)
//                        )
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                TitleText("Vital Signs and Age (computed)")
//
//                LabeledCheckbox(
//                    label = "Age > 80 years",
//                    checked = state.ageOver80Years,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "SpO2 < ${AdultTriageState.SPO2_LOW}%",
//                    checked = state.alteredVitalSignsSpo2,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "RR < ${AdultTriageState.RR_LOW}",
//                    checked = state.alteredVitalSignsRrLow,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "RR > ${AdultTriageState.RR_HIGH}",
//                    checked = state.alteredVitalSignsRrHigh,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "HR < ${AdultTriageState.HR_LOW}",
//                    checked = state.alteredVitalSignsHrLow,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "HR > ${AdultTriageState.HR_HIGH}",
//                    checked = state.alteredVitalSignsHrHigh,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "SBP < ${AdultTriageState.SBP_LOW}",
//                    checked = state.alteredVitalSignsSbpLow,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "SBP > ${AdultTriageState.SBP_HIGH}",
//                    checked = state.alteredVitalSignsSbpHigh,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Temp < ${AdultTriageState.TEMP_LOW}°C",
//                    checked = state.alteredVitalSignsTempLow,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )
//                LabeledCheckbox(
//                    label = "Temp > ${AdultTriageState.TEMP_HIGH}°C",
//                    checked = state.alteredVitalSignsTempHigh,
//                    onCheckedChange = {
//                        onEvent(
//                            TriageEvent.ComputedFieldClicked
//                        )
//                    }
//                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            FadeOverlay(Modifier.align(Alignment.BottomCenter))
        }
    }
}