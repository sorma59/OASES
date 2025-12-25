package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable to display a summary of the patient's diagnosed chronic diseases.
 */
@Composable
fun PastHistorySummary(
    state: PastMedicalHistoryState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter the list to get only the diseases that have been diagnosed.
    val diagnosedDiseases = state.diseases.filter { it.isDiagnosed == true }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            // --- Title ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Past Medical History Summary",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                IconButton(
                    onClick = { onEvent(HistoryEvent.EditButtonPressed) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit past medical history"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // --- Content ---
            if (diagnosedDiseases.isNotEmpty()) {
                // If there are diagnosed diseases, list them.
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    diagnosedDiseases.forEach { disease ->
                        DiagnosedDiseaseItem(disease = disease)
                    }
                }
            } else {
                // If no diseases are diagnosed, show a placeholder message.
                Text(
                    text = "No chronic conditions reported.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * A sub-composable to display a single diagnosed disease item.
 */
@Composable
private fun DiagnosedDiseaseItem(disease: PatientDiseaseState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 4.dp)
        )
        Column {
            // Disease Name and Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = disease.disease,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                // Show the date only if it's not blank
                if (disease.date.isNotBlank()) {
                    Text(
                        text = " (Diagnosed: ${disease.date})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            // Additional Info
            if (disease.additionalInfo.isNotBlank()) {
                Text(
                    text = disease.additionalInfo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// =====================================================================================
// Previews
// =====================================================================================

@Preview(showBackground = true)
@Composable
private fun PastHistorySummaryPreview() {
    PastHistorySummary(
        state = PastMedicalHistoryState(
            diseases = listOf(
                PatientDiseaseState(
                    disease = "Hypertension",
                    isDiagnosed = true,
                    date = "15/03/2021",
                    additionalInfo = "Controlled with medication."
                ),
                PatientDiseaseState(
                    disease = "Asthma",
                    isDiagnosed = true,
                    date = "01/01/2010",
                    additionalInfo = ""
                ),
                PatientDiseaseState(
                    disease = "Diabetes",
                    isDiagnosed = false // This one should not appear
                )
            )
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PastHistorySummaryEmptyPreview() {
    PastHistorySummary(
        state = PastMedicalHistoryState(
            diseases = listOf(
                PatientDiseaseState(disease = "Hypertension", isDiagnosed = false),
                PatientDiseaseState(disease = "Asthma", isDiagnosed = null)
            )
        ),
        onEvent = {}
    )
}