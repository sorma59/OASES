package com.unimib.oases.ui.screen.dashboard.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.ui.components.patients.PatientItem
import com.unimib.oases.ui.components.util.button.DeleteButton
import com.unimib.oases.ui.components.util.button.DismissButton
import com.unimib.oases.ui.navigation.Screen.MedicalVisit
import com.unimib.oases.ui.navigation.Screen.PatientRegistration
import com.unimib.oases.ui.navigation.Screen.SendPatient
import com.unimib.oases.ui.navigation.Screen.ViewPatientDetails
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.ToastUtils

@Composable
fun PatientDashboardScreen(
    appViewModel: AppViewModel,
    patientDashboardViewModel: PatientDashboardViewModel = hiltViewModel()
) {

    val state by patientDashboardViewModel.state.collectAsState()

    var showAlertDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        patientDashboardViewModel.navigationEvents.collect {
            appViewModel.onNavEvent(it)
        }
    }

    LaunchedEffect(Unit) {
        patientDashboardViewModel.uiEvents.collect {
            showAlertDialog = when(it){
                PatientDashboardViewModel.UiEvent.ShowDialog -> {
                    true
                }

                PatientDashboardViewModel.UiEvent.HideDialog -> {
                    false
                }
            }
        }
    }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let {
            ToastUtils.showToast(context, it)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ){

        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(64.dp)
        ) {

            PatientItem(
                patient = state.patient,
                onClick = { patientDashboardViewModel.onEvent(PatientDashboardEvent.PatientItemClicked) },
                errorText = "Could not load patient info, tap to retry \n${state.error}",
                isLoading = state.isLoading
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.End
            ) {
                for (button in state.buttons) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = button.text,
                            fontSize = 30.sp
                        )

                        Spacer(Modifier.width(4.dp))

                        IconButton(
                            onClick = {
                                patientDashboardViewModel.onEvent(
                                    PatientDashboardEvent.ActionButtonClicked(
                                        button
                                    )
                                )
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = button.buttonColor(),
                                contentColor = button.buttonTintColor()
                            ),
                            modifier = Modifier.size(Dp(ButtonDefaults.IconSize.value * 4f))
                        ) {
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

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            title = {
                Text(text = "Confirm deletion of ${state.patient?.name}")
            },
            text = {
                Text(text = "Are you sure you want to delete this patient? All the records related to this patient will be deleted.")
            },
            confirmButton = {
                DeleteButton(
                    onDelete = {
                        patientDashboardViewModel.onEvent(
                            PatientDashboardEvent.PatientDeletionConfirmed
                        )
                    }
                )
            },
            dismissButton = {
                DismissButton(
                    onDismiss = { showAlertDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    )
                )
            }
        )
    }
}

enum class PatientDashboardButton(
    val text: String,
    val icon: ImageVector,
    val contentDescription: String,
    val route: String? = null
){
    VIEW("View", Icons.Default.PersonSearch, "View patient data", ViewPatientDetails.route + "/patientId="),
    EDIT("Edit", Icons.Default.Edit, "Edit patient data", PatientRegistration.route + "?patientId="),
    SEND("Send", Icons.AutoMirrored.Filled.Send, "Send patient data", SendPatient.route + "/patientId="),
    START_VISIT("Start visit", Icons.Default.MedicalServices , "Start a new visit", MedicalVisit.route + "/patientId="),
    DELETE("Delete", Icons.Default.Delete, "Delete patient data");

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

