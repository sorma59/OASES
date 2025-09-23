package com.unimib.oases.ui.components.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.card.PatientCard
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.SmallGrayText

@Composable
fun PatientList(
    modifier: Modifier = Modifier,
    patients: List<Patient> = emptyList(),
    onItemClick: (String) -> Unit = {},
    title: String = "Patient List",
    noPatientsMessage: String = "No patients found."
) {

    val patientList = remember { mutableStateListOf<Patient>() }

    LaunchedEffect(patients) {
        patientList.clear()
        patientList.addAll(patients)
    }


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp),
    ){
        SmallGrayText(
            text = title,
            modifier = modifier.align(Alignment.Start)
        )

        if (patientList.isNotEmpty()){

            LazyVerticalGrid (
                modifier = modifier.fillMaxWidth(),
                contentPadding = PaddingValues( vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                columns = GridCells.Fixed(2),
                content = {

                    items(patientList) { patient ->
                        PatientCard(
                            patient = patient,
                            isRevealed = false,
                            onExpanded = {},
                            actions = {},
                            onCardClick = { patientId ->
                                onItemClick(patientId)
                            },
                        )
                    }
                }
            )
        } else
            CenteredText(noPatientsMessage, Modifier.padding(top = 16.dp))
    }
}