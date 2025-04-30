package com.unimib.oases.ui.patient_registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.ui.patient_registration.info.PatientInfoScreen
import com.unimib.oases.ui.patient_registration.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.patient_registration.triage.TriageScreen
import com.unimib.oases.ui.patient_registration.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.patient_registration.vital_signs.VitalSignsScreen

@Composable
fun RegistrationScreen(navController: NavController) {
    TabNavigationWithButtons(navController)
}

@Composable
fun TabNavigationWithButtons(navController: NavController) {
    val tabs = listOf("Dati anagrafici", "History", "Past Medical History", "Vital Signs", "Triage")
    var currentIndex by remember { mutableIntStateOf(0) }


    Column(modifier = Modifier.fillMaxSize()) {
        // Titolo centrale in alto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tabs[currentIndex],
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Contenuto dinamico al centro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (currentIndex) {
                0 -> PatientInfoScreen()
                1 -> VisitHistoryScreen()
                2 -> PastHistoryScreen()
                3 -> VitalSignsScreen()
                4 -> TriageScreen()
            }
        }

        // Pulsanti in basso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {
                    if (currentIndex > 0)
                        currentIndex--
                    else navController.navigate("home_screen"){
                        popUpTo(0) { inclusive = true }
                    }
                },
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    if (currentIndex < tabs.lastIndex)
                        currentIndex++
                    else {
                        navController.navigate("home_screen"){
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
            ) {
                Text(text = if (currentIndex == tabs.lastIndex) "Submit" else "Next")
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(navController = rememberNavController())
}

