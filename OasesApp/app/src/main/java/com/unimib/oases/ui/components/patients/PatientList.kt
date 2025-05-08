package com.unimib.oases.ui.components.patients

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.util.FadeOverlay
import com.unimib.oases.ui.components.util.SmallGrayText

@Composable
fun PatientList(
    patients: List<Patient>,
    navController: NavController,
    modifier: Modifier = Modifier,
    title: String = "Patient List",
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ){
        SmallGrayText(
            text = title,
            modifier = modifier.align(Alignment.Start)
        )

        Box{
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 2.dp),
                content = {
                    items(patients) { patient ->
                        PatientItem(patient, navController)
                    }
                    item { Spacer(modifier.height(64.dp)) }
                }
            )

            FadeOverlay(
                modifier = modifier.align(Alignment.BottomCenter)
            )
        }
    }
}