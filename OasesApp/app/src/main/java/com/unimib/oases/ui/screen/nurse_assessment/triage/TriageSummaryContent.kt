package com.unimib.oases.ui.screen.nurse_assessment.triage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.TriageCode
import com.unimib.oases.domain.model.symptom.Symptom.Companion.symptoms
import com.unimib.oases.ui.components.util.button.StartButton
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode

@Composable
fun TriageSummaryContent(
    state: TriageState,
    onEvent: (TriageEvent) -> Unit,
    modifier: Modifier = Modifier
){

    if (state.storedData == null){
        StartButton(
            text = "Triage yet to be performed",
            onClick = { onEvent(TriageEvent.CreateButtonPressed) }
        )
    }
    else {
        TriageCard(state.storedData, onEvent, modifier)
    }
}



@Composable
private fun TriageCard(
    data: TriageData,
    onEvent: (TriageEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Determine triage code and associated color. Handles the null case.
    val triageInfo = remember(data.triageCode) {
        when (data.triageCode) {
            TriageCode.RED -> "RED" to Color.Red
            TriageCode.YELLOW -> "YELLOW" to Color(0xFFE6A200) // A more pleasant amber/yellow
            TriageCode.GREEN -> "GREEN" to Color(0xFF2E7D32) // A darker, professional green
            TriageCode.NONE -> throw Exception("Triage code cannot be \"none\" here")
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // == 1. Triage Code Section ==
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Triage Result",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = { onEvent(TriageEvent.EditButtonPressed) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit triage"
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    tint = triageInfo.second,
                    contentDescription = "Triage color: ${triageInfo.first}",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = triageInfo.first,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = triageInfo.second
                )
            }

            // == Assigned Room Section ==
            // Only show this section if a room has actually been selected.
            data.selectedRoom?.let { room ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Assigned Room:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = room.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // == 2. Symptoms Section ==
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            if (data.selectedYellows.isEmpty() && data.selectedReds.isEmpty())
                Text("No symptom selected")
            else {
                // -- Red Symptoms --
                SymptomSection(
                    symptomIds = data.selectedReds,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(8.dp))

                // -- Yellow Symptoms --
                SymptomSection(
                    symptomIds = data.selectedYellows,
                    color = Color(0xFFE6A200)
                )
            }
        }
    }
}

/**
 * A reusable composable to display a list of symptoms.
 * Uses a FlowRow to handle wrapping symptoms to the next line if they don't fit.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SymptomSection(
    symptomIds: Set<String>,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        // Automatically wraps items to the next line.
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            symptomIds.forEach { symptomId ->
                // Find the symptom label from the static map
                val symptomLabel = symptoms[symptomId]?.label ?: "Unknown Symptom"
                SuggestionChip(
                    onClick = { /* Chips are view-only for now */ },
                    label = { Text(symptomLabel) },
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = color,
                        disabledBorderColor = Color.Black,
                        borderWidth = 1.dp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun TriageSummaryContentPreview(){
    TriageSummaryContent(
        state = TriageState(
            patientId = "",
            visitId = "",
            uiMode = PatientRegistrationScreensUiMode.Standalone(),
            storedData = TriageData(
                selectedReds = setOf("unconsciousness", "respiratory_distress", "shock", "heavy_bleeding", "severe_dehydration"),
//            selectedYellows = setOf("severe_pallor"),
                triageCode = TriageCode.RED,

                selectedRoom = Room("Emergency Room")
            )
        ),
        onEvent = { }
    )
}
