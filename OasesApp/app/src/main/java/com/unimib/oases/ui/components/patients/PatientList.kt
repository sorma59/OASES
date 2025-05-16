package com.unimib.oases.ui.components.patients

import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.ActionIcon
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.SmallGrayText
import com.unimib.oases.ui.home_page.components.card.PatientCard
import com.unimib.oases.ui.home_page.components.card.PatientUi

@Composable
fun PatientList(
    patients: List<Patient> = emptyList(),
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = "Patient List",
  //  onItemClick: (Patient) -> Unit = {},
    noPatientsMessage: String = "No patients yet"
) {

    val context = LocalContext.current


   val wrappedPatientList = remember {
        mutableStateListOf(
            *patients.map { patient ->
                PatientUi(
                    isOptionsRevealed = false,
                    item = patient
                )
            }.toTypedArray()
        )
    }

        Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
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
                                        Toast.makeText(
                                            context,
                                            "Patient ${patient.item.id} was deleted.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        wrappedPatientList.remove(patient)
                                    },
                                    backgroundColor = MaterialTheme.colorScheme.error,
                                    icon = Icons.Default.Delete,
                                    modifier = Modifier.fillMaxHeight()
                                )
                                ActionIcon(
                                    onClick = {
                                        //patients[index] = patient.copy(isOptionsRevealed = false)
                                        Toast.makeText(
                                            context,
                                            "Patients ${patient.item.id} was shared.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    backgroundColor = Color.Gray,
                                    icon = Icons.Default.Bluetooth,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            },
                            onCardClick = {
                                navController.navigate("registration_screen")
                            },
                        )
                    }
                }
            )
        } else
            CenteredText(noPatientsMessage, Modifier.padding(top = 16.dp))
    }
}