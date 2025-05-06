package com.unimib.oases.ui.screen.patient_registration

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoScreen
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.triage.NonRedCodeScreen
import com.unimib.oases.ui.screen.patient_registration.triage.TriageScreen
import com.unimib.oases.ui.screen.patient_registration.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsScreen

@Composable
fun RegistrationScreen(navController: NavController) {
    TabNavigationWithConditionalNext(navController)
}

@Composable
fun TabNavigationWithConditionalNext(navController: NavController) {
    val tabs = listOf("Dati anagrafici", "History", "Past Medical History", "Vital Signs", "Triage", "Non Red Code")
    var currentIndex by remember { mutableIntStateOf(0) }

    // Stato per tenere traccia se almeno un checkbox in TriageScreen è selezionato
    var isRedCodeSelected by remember { mutableStateOf(false) }

    // Stato per i valori dei segni vitali
    var sbpValue by remember { mutableStateOf("") }
    var dbpValue by remember { mutableStateOf("") }
    var spo2Value by remember { mutableStateOf("") }
    var hrValue by remember { mutableStateOf("") }
    var rrValue by remember { mutableStateOf("") }
    var tempValue by remember { mutableStateOf("") }

    // Stato per i valori anagrafici
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var parish by remember { mutableStateOf("") }
    var subCountry by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var nextOfKin by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    // Stato per determinare il testo del bottone "Next/Submit"
    val nextButtonText = remember(currentIndex, isRedCodeSelected) {
        if (currentIndex == tabs.lastIndex) {
            "Submit"
        } else if (currentIndex == 4 && isRedCodeSelected) {
            "Submit"
        } else {
            "Next"
        }
    }

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
                0 -> PatientInfoScreen(
                    name = name,
                    onNameChanged = { name = it },
                    age = age,
                    onAgeChanged = {
                        val filtered = it.filter { ch -> ch.isDigit() }
                        val intVal = filtered.toIntOrNull() ?: 0
                        if (intVal in 1..100 || filtered.isEmpty()) {
                            age = filtered
                        }
                    },
                    sex = sex,
                    onSexChanged = { sex = it },
                    village = village,
                    onVillageChanged = { village = it },
                    parish = parish,
                    onParishChanged = { parish = it },
                    subCountry = subCountry,
                    onSubCountryChanged = { subCountry = it },
                    district = district,
                    onDistrictChanged = { district = it },
                    nextOfKin = nextOfKin,
                    onNextOfKinChanged = { nextOfKin = it },
                    contact = contact,
                    onContactChanged = { contact = it },
                    date = date,
                    onDateChanged = { date = it },
                    time = time,
                    onTimeChanged = { time = it }
                )
                1 -> VisitHistoryScreen()
                2 -> PastHistoryScreen()
                3 -> VitalSignsScreen(
                    sbp = sbpValue,
                    dbp = dbpValue,
                    spo2 = spo2Value,
                    hr = hrValue,
                    rr = rrValue,
                    temp = tempValue,
                    onSbpChanged = { sbpValue = it },
                    onDbpChanged = { dbpValue = it },
                    onSpo2Changed = { spo2Value = it },
                    onHrChanged = { hrValue = it },
                    onRrChanged = { rrValue = it },
                    onTempChanged = { tempValue = it }
                )

                4 -> TriageScreen(
                    onRedCodeSelected = { isRedCodeSelected = it },
                    sbpValue = sbpValue,
                    dbpValue = dbpValue
                )
                5 -> NonRedCodeScreen(
                    spo2Value = spo2Value,
                    hrValue = hrValue,
                    rrValue = rrValue,
                    tempValue = tempValue
                )
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
                    if (currentIndex > 0) {
                        currentIndex--
                    } else {
                        navController.navigate("home_screen") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    if (currentIndex < tabs.lastIndex) {
                        if (currentIndex == 4 && !isRedCodeSelected) {
                            currentIndex++ // Procede solo se il codice rosso non è selezionato
                        } else if (currentIndex == 4 && isRedCodeSelected) {
                            navController.navigate("home_screen") {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            currentIndex++ // Procede al tab successivo
                        }
                    } else {
                        navController.navigate("home_screen") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
            ) {
                Text(text = nextButtonText)
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(navController = rememberNavController())
}
