package com.unimib.oases.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.patients.PatientItem
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthViewModel

@Composable
fun PatientDashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    padding: PaddingValues,
    patient: Patient
) {

    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(64.dp)
    ){
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            TitleText("Patient Dashboard")
        }

        PatientItem(
            patient = patient,
            navController = navController,
            hideBluetoothButton = true,
            onClick = {},
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.End
        ){
            for (button in PatientDashboardButton.entries) {

                Row(verticalAlignment = Alignment.CenterVertically){

                    Text(
                        text = button.text,
                        fontSize = 30.sp
                    )

                    Spacer(Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            navController.navigate(button.route)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = button.buttonColor(),
                            contentColor = button.buttonTintColor()
                        ),
                        modifier = Modifier.size(Dp(ButtonDefaults.IconSize.value * 4f))
                    ){
                        Icon(
                            imageVector = button.icon,
                            contentDescription = button.contentDescription,
                            modifier = Modifier.size(Dp(ButtonDefaults.IconSize.value * 2f))
                        )
                    }
                }
            }
        }
    }
}

enum class PatientDashboardButton(
    val text: String,
    val icon: ImageVector,
    val contentDescription: String,
    val route: String
){
    VIEW("View", Icons.Default.PersonSearch, "View patient data", Screen.RegistrationScreen.route),
    EDIT("Edit", Icons.Default.Edit, "Edit patient data", Screen.RegistrationScreen.route),
    SEND("Send", Icons.AutoMirrored.Filled.Send, "Send patient data", Screen.SendPatient.route),
    START_VISIT("Start visit", Icons.Default.MedicalServices , "Start a new visit", Screen.MedicalVisitScreen.route),
    DELETE("Delete", Icons.Default.Delete, "Delete patient data", Screen.HomeScreen.route);

    /**
     * Returns the appropriate color for this button.
     * Defaults to MaterialTheme.colorScheme.primary.
     */
    @Composable
    fun buttonColor() = when (this){
        DELETE -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    @Composable
    fun buttonTintColor() = when (this){
        DELETE -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.onPrimary
    }
}

