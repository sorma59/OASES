package com.unimib.oases.ui.screen.admin_screen.vital_signs_management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.domain.model.VitalSign
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.navigation.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitalSignManagementScreen(
    navController: NavController,
    padding : PaddingValues,
    vitalSignsManagementViewModel: VitalSignManagementViewModel = hiltViewModel(),
) {

    val state by vitalSignsManagementViewModel.state.collectAsState()



    val snackbarHostState =
        remember { SnackbarHostState() } // for hosting snackbars, if I delete a intem I get a snackbar to undo the item

    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        vitalSignsManagementViewModel.getVitalSigns()
    }




    Column(modifier = Modifier.fillMaxSize()) {


        CenterAlignedTopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Text(
                        "Admin Panel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(Screen.AdminScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Arrow back"
                    )
                }
            },
            actions = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = padding.calculateBottomPadding()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add a new vital sign:",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            OutlinedTextField(
                value = state.vitalSign.name,
                onValueChange = { vitalSignsManagementViewModel.onEvent(VitalSignManagementEvent.EnteredVitalSignName(it)) },
                label = { Text("Name") },
                placeholder = { Text("e.g. Systolic Blood Pressure, ...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.vitalSign.acronym,
                onValueChange = { vitalSignsManagementViewModel.onEvent(VitalSignManagementEvent.EnteredVitalSignAcronym(it)) },
                label = { Text("Acronym") },
                placeholder = { Text("e.g. SBP, SpO2, ...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.vitalSign.unit,
                onValueChange = { vitalSignsManagementViewModel.onEvent(VitalSignManagementEvent.EnteredVitalSignUnit(it)) },
                label = { Text("Unit") },
                placeholder = { Text("e.g. mmHg, bpm, %, ...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {

//                        if (state.disease.username.isBlank() || state.dise.pwHash.isBlank()) {
//                            state.error = "Username and password cannot be empty!"
//                            return@Button
//                        }

                    vitalSignsManagementViewModel.onEvent(VitalSignManagementEvent.SaveVitalSign)

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Vital Sign")
            }


            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            state.message?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {


                Text(
                    text = "Vital Signs (${state.vitalSigns.size})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )


                if (state.isLoading) {
                    CustomCircularProgressIndicator()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(state.vitalSigns.size) { i ->
                            val vitalSign = state.vitalSigns[i]
                            VitalSignListItem (
                                vitalSign = vitalSign,
                                onDelete = {
                                    vitalSignsManagementViewModel.onEvent(VitalSignManagementEvent.Delete(vitalSign))
                                    scope.launch {
                                        val undo = snackbarHostState.showSnackbar(
                                            message = "Deleted Vital Sign ${vitalSign.name}",
                                            actionLabel = "UNDO"
                                        )
                                        if (undo == SnackbarResult.ActionPerformed) {
                                            vitalSignsManagementViewModel.onEvent(VitalSignManagementEvent.UndoDelete)
                                        }
                                    }
                                },
                                onClick = {
                                    vitalSignsManagementViewModel.onEvent(VitalSignManagementEvent.Click(vitalSign))
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun VitalSignListItem(
    vitalSign: VitalSign,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = vitalSign.name,
                    style = MaterialTheme.typography.bodyLarge
                )
//                if (user.username.isNotEmpty()) {
//                    Text(
//                        text = user.username,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                    )
//                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete disease",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}