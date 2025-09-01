package com.unimib.oases.ui.components.patients

import androidx.compose.runtime.Composable
import com.unimib.oases.domain.model.Patient

@Composable
fun RecentlyReceivedPatientList(
    patients: List<Patient>,
){

    PatientList(
        patients = patients,
        title = "Recently Received Patients",
        noPatientsMessage = "No patients received yet",
    )
}