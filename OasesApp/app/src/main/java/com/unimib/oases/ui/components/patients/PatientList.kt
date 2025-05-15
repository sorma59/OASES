package com.unimib.oases.ui.components.patients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.SmallGrayText

@Composable
fun PatientList(
    patients: List<Patient>,
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = "Patient List",
    onItemClick: (Patient) -> Unit = {},
    noPatientsMessage: String = "No patients yet"
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ){
        SmallGrayText(
            text = title,
            modifier = modifier.align(Alignment.Start)
        )

        if (patients.isNotEmpty()){

            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 2.dp),
                content = {
                    items(patients) { patient ->
                        PatientItem(patient, navController, onClick = onItemClick)
                    }
                }
            )
        } else
            CenteredText(noPatientsMessage, Modifier.padding(top = 16.dp))
    }
}