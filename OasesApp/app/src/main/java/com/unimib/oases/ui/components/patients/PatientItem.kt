package com.unimib.oases.ui.components.patients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.util.StringFormatHelper.getAgeWithSuffix

@Composable
fun PatientItem(
    patient: Patient,
    navController: NavController,
    modifier: Modifier = Modifier,
    hideBluetoothButton: Boolean = false,
    onClick: (Patient) -> Unit = {}
){
    val ageString = getAgeWithSuffix(patient.ageInMonths)

    Card(
        onClick = {onClick(patient)},
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .height(80.dp)
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.primary),
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    TitleText(patient.name + ", " + ageString, color = MaterialTheme.colorScheme.onPrimary)
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .fillMaxWidth()
                ){
                    Text(patient.publicId, color = MaterialTheme.colorScheme.onPrimary)
                    Text(patient.status, color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            if (!hideBluetoothButton)
                BluetoothButton(patient, navController)
        }
    }
}

@Composable
fun BluetoothButton(patient: Patient, navController: NavController) {

    val route = Screen.SendPatient.route

    IconButton(
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("patient", patient)
            navController.navigate(route)
        },
    ) {
        Icon(
            imageVector = Icons.Default.Bluetooth,
            contentDescription = "Send Patient via Bluetooth",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
