package com.unimib.oases.ui.components.patients

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.screen.homepage.HomeScreenViewModel

@Composable
fun RecentlyReceivedPatientList(
    patients: List<Patient>,
    navController: NavController
){

    // temporary
    val homeScreenViewModel : HomeScreenViewModel = hiltViewModel()

    PatientList(
        patients = patients,
        navController = navController,
        title = "Recently Received Patients",
        noPatientsMessage = "No patients received yet",
        homeScreenViewModel = homeScreenViewModel
    )
}