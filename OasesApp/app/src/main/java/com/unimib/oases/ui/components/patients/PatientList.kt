package com.unimib.oases.ui.components.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.ActionIcon
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.SmallGrayText
import com.unimib.oases.ui.home_page.components.card.PatientCard
import com.unimib.oases.ui.home_page.components.card.PatientUi
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.homepage.HomeScreenEvent
import com.unimib.oases.ui.screen.homepage.HomeScreenViewModel

@Composable
fun PatientList(
    modifier: Modifier = Modifier,
    patients: List<Patient> = emptyList(),
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel,
    title: String = "Patient List",
  //  onItemClick: (Patient) -> Unit = {},
    noPatientsMessage: String = "No patients found."
) {

    val wrappedPatientList = remember { mutableStateListOf<PatientUi>() }


    // State per controllare la visibilità dell'alert dialog
    var showAlertDialog by remember { mutableStateOf(false) }


    LaunchedEffect(patients) {
        wrappedPatientList.clear()
        wrappedPatientList.addAll(
            patients.map { patient ->
                PatientUi(
                    isOptionsRevealed = false,
                    item = patient
                )
            }
        )
    }


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp),

    ){
        SmallGrayText(
            text = title,
            modifier = modifier.align(Alignment.Start)
        )

        if (wrappedPatientList.isNotEmpty()){

            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                contentPadding = PaddingValues( vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {

                    items(wrappedPatientList) { patient ->
                        //PatientItem(patient, navController, onClick = onItemClick)
                        PatientCard(
                            patient = patient.item,
                            isRevealed = patient.isOptionsRevealed,
                            onExpanded = {
                                wrappedPatientList.replaceAll { p ->
                                    p.copy(isOptionsRevealed = p.item.id == patient.item.id)
                                }
                            },
                            onCollapsed = {
                                //wrappedPatientList[index] = patient.copy(isOptionsRevealed = false)
                            },
                            actions = {
                                ActionIcon(
                                    onClick = {
                                        showAlertDialog = true
                                       // homeScreenViewModel.onEvent(HomeScreenEvent.Delete(patient.item))
//                                        wrappedPatientList.remove(patient)
                                    },
                                    backgroundColor = MaterialTheme.colorScheme.error,
                                    icon = Icons.Default.Delete,
                                    modifier = Modifier.fillMaxHeight()
                                )
                                ActionIcon(
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("patient", patient.item)
                                        navController.navigate(Screen.SendPatient.route)
                                    },
                                    backgroundColor = Color.Gray,
                                    icon = Icons.Default.Bluetooth,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            },
                            onCardClick = {
                                navController.navigate(Screen.RegistrationScreen.route + "?patientId=${patient.item.id}")
                            },
                        )

                        // Alert Dialog
                        if (showAlertDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    // Chiudi il dialog senza salvare
                                    showAlertDialog = false
                                },
                                title = {
                                    Text(text = "Confirm deletion of ${patient.item.name}")
                                },
                                text = {
                                    Text(text = "Are you sure you want to delete this patient? All the records associated with this patient will be deleted.")
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            // Chiudi il dialog e quindi CHIEDI al ViewModel di fare il salvataggio finale sul DB
                                            showAlertDialog = false
                                            homeScreenViewModel.onEvent(HomeScreenEvent.Delete(patient.item))
                                        }
                                    ) {
                                        Text("Confirm")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            // Chiudi il dialog senza fare nulla
                                            showAlertDialog = false
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                    }
                }
            )
        } else
            CenteredText(noPatientsMessage, Modifier.padding(top = 16.dp))
    }
}