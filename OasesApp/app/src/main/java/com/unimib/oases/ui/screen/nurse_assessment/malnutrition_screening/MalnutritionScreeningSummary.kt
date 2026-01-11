package com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.MuacCategory
import com.unimib.oases.ui.components.card.OasesCard
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode

@Composable
fun MalnutritionScreeningSummary(
    data: MalnutritionScreeningData,
    onEvent: (MalnutritionScreeningEvent) -> Unit,
    modifier: Modifier
) {
    val decimalFormat = remember { DecimalFormat("#.#") }

    val listItemColors = ListItemDefaults.colors(
        containerColor = CardDefaults.cardColors().containerColor
    )

    OasesCard(
        modifier.fillMaxWidth(),
        shape = CardDefaults.elevatedShape
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            // --- Title ---
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Malnutrition Screening Summary",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                IconButton(
                    onClick = { onEvent(MalnutritionScreeningEvent.EditButtonPressed) },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit malnutrition screening"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))

            // --- Weight, Height, BMI ---
            ListItem(
                headlineContent = { Text("Weight") },
                leadingContent = {
                    Icon(
                        Icons.Default.MonitorWeight,
                        contentDescription = "Weight"
                    )
                },
                trailingContent = {
                    Text(
                        text = data.weight.takeIf { it.isNotBlank() }?.let { "$it kg" }
                            ?: "N/A",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = listItemColors
            )

            ListItem(
                headlineContent = { Text("Height") },
                leadingContent = {
                    Icon(
                        Icons.Default.Height,
                        contentDescription = "Height"
                    )
                },
                trailingContent = {
                    Text(
                        text = data.height.takeIf { it.isNotBlank() }?.let { "$it cm" }
                            ?: "N/A",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = listItemColors
            )

            ListItem(
                headlineContent = { Text("BMI (Body Mass Index)") },
                leadingContent = {
                    Icon(
                        Icons.Default.SquareFoot,
                        contentDescription = "BMI"
                    )
                },
                trailingContent = {
                    Text(
                        text = data.bmi?.let { decimalFormat.format(it) } ?: "N/A",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = listItemColors
            )

            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // --- MUAC Section ---
            MuacResult(
                value = data.muacState.value,
                category = data.muacState.category
            )
        }
    }
}

/**
 * A sub-composable to display the MUAC result with its corresponding color code.
 */
@Composable
private fun MuacResult(
    value: String,
    category: MuacCategory?,
    modifier: Modifier = Modifier
) {
    val muacColor = remember(category) {
        when (category) {
            MuacCategory.SEVERE -> Color.Red
            MuacCategory.MODERATE -> Color(0xFFE6A200) // Amber
            MuacCategory.NORMAL -> Color(0xFF2E7D32) // Dark Green
            null -> Color.Gray
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Straighten, contentDescription = "MUAC")
            Text("MUAC (Mid-Upper Arm Circumference)")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value.takeIf { it.isNotBlank() }?.let { "$it cm" } ?: "N/A",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "MUAC Category: ${category?.name}",
                tint = muacColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MalnutritionSummaryPreview() {
    MalnutritionSummary(
        state = MalnutritionScreeningState(
            patientId = "",
            visitId = "",
            uiMode = PatientRegistrationScreensUiMode.Standalone(),
            storedData = MalnutritionScreeningData(
                weight = "68.5",
                height = "175",
                bmi = 22.3,
                muacState = MuacState(
                    value = "28.5",
                    category = MuacCategory.NORMAL
                )
            )
        ),
        {}
    )
}

@Preview(showBackground = true)
@Composable
fun MalnutritionSummaryIncompletePreview() {
    MalnutritionSummary(
        state = MalnutritionScreeningState(
            patientId = "",
            visitId = "",
            uiMode = PatientRegistrationScreensUiMode.Standalone(),
            storedData = MalnutritionScreeningData(
                weight = "",
                height = "160",
                bmi = null,
                muacState = MuacState(
                    value = "21.2",
                    category = MuacCategory.MODERATE
                )
            )
        ),
        {}
    )
}