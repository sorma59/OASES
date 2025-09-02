package com.unimib.oases.ui.screen.dashboard.patient.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.scaffold.OasesTopAppBar
import com.unimib.oases.ui.components.scaffold.OasesTopAppBarType
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.button.EditButton
import com.unimib.oases.ui.components.util.button.RetryButton
import com.unimib.oases.ui.navigation.NavigationEvent
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.util.StringFormatHelper.getAgeWithSuffix

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    padding: PaddingValues
) {

    val viewModel: PatientDetailsViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect {
            when (it){
                is NavigationEvent.Navigate -> {
                    navController.navigate(it.route)
                }
                is NavigationEvent.NavigateBack -> {
                    navController.popBackStack()
                }

//                NavigationEvent.NavigateToLogin -> {
//                    navController.navigateToLogin()
//                }

//                is NavigationEvent.NavigateAfterLogin -> Unit
            }
        }
    }

    Column(Modifier.fillMaxSize()){

        OasesTopAppBar(
            title = "Patient Details",
            type = OasesTopAppBarType.BACK,
            onNavigationIconClick = { viewModel.onEvent(PatientDetailsEvent.OnBack) },
            actions = {
                EditButton(
                    onClick = { viewModel.onEvent(PatientDetailsEvent.OnEdit) },
                    contentDescription = "Edit patient details"
                )
            }
        )

        state.patient?.let {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(bottom = padding.calculateBottomPadding())
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(24.dp))

                Box(
                    modifier = Modifier.weight(1f)
                ) {
//                        EditButton(
//                            onClick = { viewModel.onEvent(PatientDetailsEvent.OnEdit) },
//                            contentDescription = "Edit patient details"
//                        ) // Top Right

                    PatientDetails(it)
                }

                BottomButtons(
                    onCancel = { viewModel.onEvent(PatientDetailsEvent.OnBack) },
                    onConfirm = { viewModel.onEvent(PatientDetailsEvent.OnNext) },
                    cancelButtonText = "Back",
                    confirmButtonText = "Next",
                )
            }
        }
            ?: Box(Modifier.fillMaxSize()) {
                RetryButton(
                    error = "Failed to load patient data",
                    onClick = { viewModel.onEvent(PatientDetailsEvent.OnRetry) },
                )
            }
    }
}

@Composable
private fun PatientDetails(patient: Patient) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = patient.name,
                fontSize = 24.sp,
            )

            Text(
                text = patient.sex,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val age = getAgeWithSuffix(patient.ageInMonths)

            Text(
                text = age,
                fontSize = 16.sp
            )

            Text(
                text = patient.birthDate,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        val location = listOf(
            patient.village,
            patient.parish,
            patient.subCounty,
            patient.district
        ).joinToString()

        Text(
            text = patient.contact,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = location,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(Modifier.height(6.dp))

        HorizontalDivider()

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Next of kin"
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = patient.nextOfKin,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.MedicalServices,
                contentDescription = "Next of kin"
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = patient.status,
                fontSize = 14.sp
            )
        }
    }
}