package com.unimib.oases.ui.screen.dashboard.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.ui.components.patients.PatientItem
import com.unimib.oases.ui.components.util.button.DeleteButton
import com.unimib.oases.ui.components.util.button.DismissButton
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun PatientDashboardScreen(
    appViewModel: AppViewModel,
    viewModel: PatientDashboardViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isLoading)

    PatientDashboardContent(state, viewModel::onEvent)
}

@Composable
private fun PatientDashboardContent(
    state: PatientDashboardState,
    onEvent: (PatientDashboardEvent) -> Unit
) {

    val scrollState = rememberScrollState()

    state.error?.let {
        RetryButton(it) {
            onEvent(PatientDashboardEvent.Refresh)
        }
    } ?: Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(64.dp)
    ) {

        PatientItem(state.patientWithVisitInfo)

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.End
        ) {
            for (action in state.actions) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = action.text,
                        textAlign = TextAlign.End,
                        fontSize = 30.sp,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(Modifier.width(4.dp))

                    ActionButton(
                        action
                    ) {
                        onEvent(
                            PatientDashboardEvent.ActionButtonClicked(
                                action
                            )
                        )
                    }
                }
            }
        }
    }

    if (state.showAlertDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(PatientDashboardEvent.PatientDeletionCancelled) },
            title = {
                Text(text = "Confirm deletion of ${state.patientWithVisitInfo?.patient?.name}")
            },
            text = {
               Text("Are you sure you want to delete this patient? All the records related to this patient will be deleted.")
           },
            confirmButton = {
                DeleteButton(
                    text = "Delete",
                    onDelete = {
                        onEvent(
                            PatientDashboardEvent.PatientDeletionConfirmed
                        )
                    }
                )
            },
            dismissButton = {
                DismissButton(
                    onDismiss = { onEvent(PatientDashboardEvent.PatientDeletionCancelled) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    )
                )
            }
        )
    }
}

@Composable
fun ActionButton(
    action: PatientDashboardAction,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = action.buttonColor(),
            contentColor = action.buttonTintColor()
        ),
        modifier = Modifier.size(Dp(ButtonDefaults.IconSize.value * 4f))
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = action.contentDescription,
            modifier = Modifier.size(Dp(ButtonDefaults.IconSize.value * 2f))
        )
    }
}

sealed interface PatientDashboardAction {
    val text: String
    val icon: ImageVector
    val contentDescription: String
    val roles: List<Role>

    companion object {
        val entries: List<PatientDashboardAction> by lazy {
            listOf(
                Demographics,
                Triage,
                VitalSigns,
                MalnutritionScreening,
                History,
                StartVisit,
                Send,
                Delete
            )
        }
    }

    /**
     * A sealed sub-interface specifically for buttons that navigate.
     * Its primary responsibility is to create a Route.
     */
    sealed interface Navigable : PatientDashboardAction

    sealed interface PatientNavigable: Navigable {
        fun createRoute(patientId: String): Route
    }

    /**
     * A data object for buttons that perform an on-screen action, like showing a dialog.
     */
    data object Delete : PatientDashboardAction {
        override val text = "Delete"
        override val icon = Icons.Default.Delete
        override val contentDescription = "Delete patient data"
        override val roles = Role.entries
    }

    // --- Concrete implementations of Navigable actions ---
    data object Demographics: PatientNavigable {
        override val text = "Demographics"
        override val icon = Icons.Default.PersonSearch
        override val contentDescription = "Demographics"
        override val roles = Role.entries
        override fun createRoute(patientId: String) = Route.Demographics(patientId)
    }

    data object Triage: Navigable {
        override val text = "Triage"
        override val icon = Icons.Default.PriorityHigh
        override val contentDescription = "Triage"
        override val roles = Role.entries
        // Editing demographics for an existing patient
        fun createRoute(patientId: String, visitId: String) = Route.Triage(patientId, visitId)
    }

    data object VitalSigns: Navigable {
        override val text = "Vital Signs"
        override val icon = Icons.Default.PriorityHigh
        override val contentDescription = "Vital Signs"
        override val roles = Role.entries

        fun createRoute(patientId: String, visitId: String) = Route.VitalSigns(patientId, visitId)
    }

    data object VitalSignsForm: Navigable {
        override val text = "Vital Signs Form"
        override val icon = Icons.Default.PriorityHigh
        override val contentDescription = "Vital Signs Form"
        override val roles = Role.entries

        fun createRoute(patientId: String, visitId: String) = Route.VitalSignsForm
    }



    data object MalnutritionScreening: Navigable {
        override val text = "Malnutrition Screening"
        override val icon = Icons.Default.Straighten
        override val contentDescription = "Malnutrition Screening"
        override val roles = Role.entries

        fun createRoute(patientId: String, visitId: String) = Route.MalnutritionScreening(
            patientId, visitId
        )
    }



    data object History: PatientNavigable {
        override val text = "History"
        override val icon = Icons.Default.History
        override val contentDescription = "History"
        override val roles = Role.entries

        override fun createRoute(patientId: String) = Route.History(patientId)
    }

    data object Send : Navigable {
        override val text = "Send"
        override val icon = Icons.AutoMirrored.Filled.Send
        override val contentDescription = "Send patient data"
        override val roles = Role.entries
        fun createRoute(patientId: String, visitId: String) = Route.SendPatient(patientId, visitId)
    }

    data object StartVisit : PatientNavigable {
        override val text = "Start visit"
        override val icon = Icons.Default.MedicalServices
        override val contentDescription = "Start a new visit"
        override val roles = listOf(Role.DOCTOR)
        override fun createRoute(patientId: String) = Route.MedicalVisit(patientId)
    }

    // --- UI Logic common to all actions ---
    @Composable
    fun buttonColor() = when (this) {
        is Delete -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    @Composable
    fun buttonTintColor() = when (this) {
        is Delete -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.onPrimary
    }
}

