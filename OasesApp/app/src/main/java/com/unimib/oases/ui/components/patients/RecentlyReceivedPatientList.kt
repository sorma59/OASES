package com.unimib.oases.ui.components.patients

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient

@Composable
fun RecentlyReceivedPatientList(
    patients: List<Patient>,
    navController: NavController
){
    PatientList(
        patients = patients,
        navController = navController,
        title = "Recently Received Patients",
        noPatientsMessage = "No patients received yet"
    )
}