package com.unimib.oases.ui.screen.patient_registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.patient_registration.info.PatientInfoScreen
import com.unimib.oases.ui.screen.patient_registration.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.triage.NonRedCodeScreen
import com.unimib.oases.ui.screen.patient_registration.triage.TriageScreen
import com.unimib.oases.ui.screen.patient_registration.visit_history.VisitHistoryScreen
import com.unimib.oases.ui.screen.patient_registration.vital_signs.VitalSignsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    padding: PaddingValues
) {

    val registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel()


    val tabs = listOf(
        "Dati anagrafici",
        "History",
        "Past Medical History",
        "Vital Signs",
        "Triage",
        "Non Red Code"
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    var isRedCodeSelected by remember { mutableStateOf(false) }

    var sbpValue by remember { mutableStateOf("") }
    var dbpValue by remember { mutableStateOf("") }
    var spo2Value by remember { mutableStateOf("") }
    var hrValue by remember { mutableStateOf("") }
    var rrValue by remember { mutableStateOf("") }
    var tempValue by remember { mutableStateOf("") }
    var rbsValue by remember { mutableStateOf("") }

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

    var showDialog by remember { mutableStateOf(false) }

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
        CenterAlignedTopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Patient Registration",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(Screen.HomeScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Arrow back"
                    )
                }
            },
            actions = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
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
                        rbs = rbsValue,
                        onSbpChanged = { sbpValue = it },
                        onDbpChanged = { dbpValue = it },
                        onSpo2Changed = { spo2Value = it },
                        onHrChanged = { hrValue = it },
                        onRrChanged = { rrValue = it },
                        onTempChanged = { tempValue = it },
                        onRbsChanged = { rbsValue = it }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (currentIndex > 0) {
                        OutlinedButton(onClick = { currentIndex-- }) {
                            Text("Back")
                        }
                    }
                }

                Column {
                    Button(
                        onClick = {
                            if (currentIndex == 0) {
                                showDialog = true
                            } else if (currentIndex < tabs.lastIndex) {
                                if (currentIndex == 4 && !isRedCodeSelected) {
                                    currentIndex++
                                } else if (currentIndex == 4 ) {
                                    navController.navigate(Screen.HomeScreen.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    currentIndex++
                                }
                            } else {
                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Text(text = nextButtonText)
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Conferma") },
                    text = { Text("Vuoi continuare con la registrazione?") },
                    confirmButton = {
                        Button(onClick = {
                            showDialog = false
                            currentIndex++
                        }) {
                            Text("Sì")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                showDialog = false
                                val patient =
                                    Patient(
                                        name = name,
                                        age = age.toInt(),
                                        sex = sex,
                                        village = village,
                                        parish = parish,
                                        subCounty = subCountry,
                                        district = district,
                                        nextOfKin = nextOfKin,
                                        contact = contact
                                    )
                                registrationScreenViewModel.addPatient(patient)
                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        ) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}
