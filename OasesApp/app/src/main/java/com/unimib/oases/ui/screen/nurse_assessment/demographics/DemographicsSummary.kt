package com.unimib.oases.ui.screen.nurse_assessment.demographics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.unit.sp
import com.unimib.oases.ui.components.card.OasesCard
import com.unimib.oases.ui.screen.nurse_assessment.PatientRegistrationScreensUiMode
import com.unimib.oases.util.StringFormatHelper.getAgeWithSuffix

@Composable
fun DemographicsSummary(
    state: DemographicsState,
    onEvent: (DemographicsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    OasesCard(modifier) {
        // Add padding inside the card to give the content some breathing room.
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = state.storedData.name,
                        fontSize = 24.sp,
                        // Suggestion: Making the name bold helps it stand out as the primary info.
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = state.storedData.sexOption.displayName,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                IconButton(
                    onClick = { onEvent(DemographicsEvent.EditButtonPressed) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit triage"
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val age = getAgeWithSuffix(state.storedData.ageInMonths)

                Text(
                    text = age,
                    fontSize = 16.sp
                )

                Text(
                    text = "(${state.storedData.birthDate})",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(Modifier.height(12.dp))

            val location = listOfNotNull(
                state.storedData.village.takeIf { it.isNotBlank() },
                state.storedData.parish.takeIf { it.isNotBlank() },
                state.storedData.subCounty.takeIf { it.isNotBlank() },
                state.storedData.district.takeIf { it.isNotBlank() }
            ).joinToString(", ")

            Text(
                text = state.storedData.contact,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.height(14.dp), // Match icon size with text
                    tint = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = location,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }


            Spacer(Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Next of kin"
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = state.storedData.nextOfKin,
                    fontSize = 14.sp
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
private fun DemographicsSummaryFullPreview() {
    DemographicsSummary(
        state = DemographicsState(
            storedData = PatientData(
                id = "patient123",
                name = "Ettore Zini",
                birthDate = "22/10/1998",
                ageInMonths = 310, // Approx 25 years
                sexOption = SexOption.fromSex(Sex.MALE),
                village = "Kampala",
                parish = "Central",
                subCounty = "Kawempe",
                district = "Kampala",
                nextOfKin = "Jane Doe (Mother)",
                contact = "+256 777 123456"
            ),
            uiMode = PatientRegistrationScreensUiMode.Standalone(isEditing = false)
        ),
        onEvent = {} // In a preview, the event handler can be empty
    )
}

@Preview(showBackground = true)
@Composable
private fun DemographicsSummaryPartialPreview() {
    DemographicsSummary(
        state = DemographicsState(
            storedData = PatientData(
                id = "patient456",
                name = "Alice Smith",
                birthDate = "10/05/2021",
                ageInMonths = 37, // Approx 3 years
                sexOption = SexOption.fromSex(Sex.FEMALE),
                village = "", // Empty village
                parish = "West",
                subCounty = "Makindye",
                district = "Kampala",
                nextOfKin = "Robert Smith (Father)",
                contact = "" // Empty contact
            ),
            uiMode = PatientRegistrationScreensUiMode.Standalone(isEditing = false)
        ),
        onEvent = {}
    )
}
