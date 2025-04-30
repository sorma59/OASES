package com.unimib.oases.ui.patient_registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unimib.oases.ui.patient_registration.info.PatientInfoScreen
import com.unimib.oases.ui.patient_registration.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.patient_registration.triage.TriageScreen
import com.unimib.oases.ui.patient_registration.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.patient_registration.vital_signs.VitalSignsScreen

@Composable
fun RegistrationScreen() {
    TabNavigationWithButtons()
}

@Composable
fun TabNavigationWithButtons() {
    val tabs = listOf("Dati anagrafici", "History", "Past Medical History", "Vital Signs", "Triage")
    val selectedTabIndexState = remember { mutableIntStateOf(0) }
    var selectedTabIndex = selectedTabIndexState.intValue

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tab layout visivo
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {}, // disattivato
                    enabled = false,
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenuto centrato della pagina
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTabIndex) {
                0 -> PatientInfoScreen()
                1 -> VisitHistoryScreen()
                2 -> PastHistoryScreen()
                3 -> VitalSignsScreen()
                4 -> TriageScreen()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pulsanti in basso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (selectedTabIndex > 0) selectedTabIndex-- },
                enabled = selectedTabIndex > 0
            ) {
                Text("Back")
            }

            Button(
                onClick = { if (selectedTabIndex < tabs.lastIndex) selectedTabIndex++ },
                enabled = selectedTabIndex < tabs.lastIndex
            ) {
                Text("Next")
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}

