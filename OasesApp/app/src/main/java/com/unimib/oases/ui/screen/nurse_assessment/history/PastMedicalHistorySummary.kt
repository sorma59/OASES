package com.unimib.oases.ui.screen.nurse_assessment.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.components.card.OasesCard

/**
 * A composable to display a summary of the patient's diagnosed chronic diseases.
 */
@Composable
fun PastHistorySummary(
    freeTextDiseases: List<PatientDiseaseState>,
    selectionDiseases: List<PatientDiseaseState>,
    onEvent: (HistoryEvent) -> Unit,
    shouldShowEditButton: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val documentedConditions = freeTextDiseases
        .filter { it.freeTextValue.isNotBlank() }

    val diagnosedDiseases = selectionDiseases
        .filter { it.isDiagnosed == true } // Simplified this filter, as freeText is handled above
        .sortedByDescending { it.date }

    OasesCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- Title ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Past Medical History Summary",
                    style = MaterialTheme.typography.titleMedium
                )

                if (shouldShowEditButton()) {
                    IconButton(
                        onClick = { onEvent(HistoryEvent.EditButtonPressed) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit past medical history"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            // --- Documented Content ---
            if (documentedConditions.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    documentedConditions.forEach { disease ->
                        FreeTextConditionItem(disease = disease)
                    }
                }
                // Add a divider if there are also diagnosed diseases to show
                if (diagnosedDiseases.isNotEmpty()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }
            }

            // --- Diagnosed Content ---
            if (diagnosedDiseases.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    diagnosedDiseases.forEach { disease ->
                        DiagnosedDiseaseItem(disease = disease)
                    }
                }
            } else if (documentedConditions.isEmpty()) {
                // Show this text only if BOTH lists are empty
                Text("No known chronic diseases")
            }
        }
    }
}

/**
 * A sub-composable for displaying a free-text documented condition.
 */
@Composable
private fun FreeTextConditionItem(disease: PatientDiseaseState) {
    Row(
        verticalAlignment = Alignment.Top, // Align icon to the top of the text
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Notes,
            contentDescription = "Documented condition",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 4.dp)
        )
        Column {
            Text(
                text = disease.disease, // e.g., "Surgical History"
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = disease.freeTextValue, // The actual free text entered by the user
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


/**
 * A sub-composable to display a single answered disease item,
 * showing if it was diagnosed or not.
 */
@Composable
private fun DiagnosedDiseaseItem(
    disease: PatientDiseaseState
) {
    // ... (This composable remains unchanged)
    val icon =Icons.Default.CheckCircle
    val iconColor = MaterialTheme.colorScheme.primary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Diagnosed",
            tint = iconColor,
            modifier = Modifier.padding(top = 4.dp)
        )
        Column {
            // Disease Name and Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = disease.disease,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Show the date only if the date is not blank
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
// Previews (Also updated to reflect the new logic)
// =====================================================================================

//@Preview(showBackground = true)
//@Composable
//private fun PastHistorySummaryPreview() {
//    PastHistorySummary(
//        selectionDiseases = listOf(
//            PatientDiseaseState(
//                disease = "Hypertension",
//                group = PmhGroup.NEUROPSYCHIATRIC,
//                entryType = DiseaseEntryType.FREE_TEXT,
//                isDiagnosed = true,
//                date = "15/03/2021",
//                additionalInfo = "Controlled with medication."
//            ),
//            PatientDiseaseState(
//                disease = "Asthma",
//                group = PmhGroup.NEUROPSYCHIATRIC,
//                entryType = DiseaseEntryType.FREE_TEXT,
//                isDiagnosed = true,
//                date = "01/01/2010",
//                additionalInfo = ""
//            ),
//            PatientDiseaseState(
//                disease = "Diabetes",
//                group = PmhGroup.NEUROPSYCHIATRIC,
//                entryType = DiseaseEntryType.FREE_TEXT,
//                isDiagnosed = false
//            ),
//            PatientDiseaseState(
//                disease = "Allergies",
//                group = PmhGroup.NEUROPSYCHIATRIC,
//                entryType = DiseaseEntryType.FREE_TEXT,
//                isDiagnosed = null
//            )
//        ),
//        onEvent = {},
//        shouldShowEditButton = { true }
//    )
//}

//@Preview(showBackground = true)
//@Composable
//private fun PastHistorySummaryEmptyPreview() {
//    PastHistorySummary(
//        selectionDiseases = listOf(
//            PatientDiseaseState(
//                disease = "Hypertension",
//                group = PmhGroup.NEUROPSYCHIATRIC,
//                entryType = DiseaseEntryType.FREE_TEXT,
//                isDiagnosed = null
//            ),
//            PatientDiseaseState(
//                disease = "Asthma",
//                group = PmhGroup.NEUROPSYCHIATRIC,
//                entryType = DiseaseEntryType.FREE_TEXT,
//                isDiagnosed = null
//            )
//        ),
//        onEvent = {},
//        shouldShowEditButton = { true }
//    )
//}
