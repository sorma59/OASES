package com.unimib.oases.ui.screen.dashboard.admin.disease

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.Disease
import com.unimib.oases.domain.model.SexSpecificity
import com.unimib.oases.ui.components.util.OutlinedDropdown
import com.unimib.oases.ui.components.util.button.DeleteButton
import com.unimib.oases.ui.components.util.button.DismissButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import kotlinx.coroutines.launch

@Composable
fun DiseaseManagementScreen() {

    val diseaseManagementViewModel: DiseaseManagementViewModel = hiltViewModel()

    val state by diseaseManagementViewModel.state.collectAsState()

    DiseaseManagementContent(state, diseaseManagementViewModel)
}

@Composable
private fun DiseaseManagementContent(
    state: DiseaseManagementState,
    diseaseManagementViewModel: DiseaseManagementViewModel
) {
    val snackbarHostState =
        remember { SnackbarHostState() } // for hosting snackbars, if I delete a item I get a snackbar to undo the item

    val scope = rememberCoroutineScope()

    var showDeletionDialog by remember { mutableStateOf(false) }

    var diseaseToDelete by remember { mutableStateOf<Disease?>(null) }

    val dismissDeletionDialog = {
        showDeletionDialog = false
        diseaseToDelete = null
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
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
                    text = "Add a new disease:",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            OutlinedTextField(
                value = state.disease.name,
                onValueChange = {
                    diseaseManagementViewModel.onEvent(
                        DiseaseManagementEvent.EnteredDiseaseName(
                            it
                        )
                    )
                },
                label = { Text("Disease") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedDropdown(
                selected = state.disease.sexSpecificity.displayName,
                onSelected = {
                    diseaseManagementViewModel.onEvent(
                        DiseaseManagementEvent.EnteredSexSpecificity(
                            it
                        )
                    )
                },
                options = SexSpecificity.entries.map { it.displayName },
                labelText = "Sex specificity",
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedDropdown(
                selected = state.disease.ageSpecificity.displayName,
                onSelected = {
                    diseaseManagementViewModel.onEvent(
                        DiseaseManagementEvent.EnteredAgeSpecificity(
                            it
                        )
                    )
                },
                options = AgeSpecificity.entries.map { it.displayName },
                labelText = "Age specificity",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    diseaseManagementViewModel.onEvent(DiseaseManagementEvent.SaveDisease)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Disease")
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
                    text = "Diseases (${state.diseases.size})",
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
                        items(state.diseases.size) { i ->
                            val disease = state.diseases[i]
                            DiseaseListItem(
                                disease = disease,
                                onDelete = {
                                    diseaseToDelete = disease
                                    showDeletionDialog = true
                                },
                                onClick = {
                                    diseaseManagementViewModel.onEvent(
                                        DiseaseManagementEvent.Click(
                                            disease
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeletionDialog) {
        AlertDialog(
            onDismissRequest = dismissDeletionDialog,
            title = { "Delete Disease" },
            text = { Text("Are you sure you want to delete this disease?") },
            confirmButton = {
                DeleteButton(
                    onDelete = {
                        diseaseManagementViewModel.onEvent(
                            DiseaseManagementEvent.Delete(
                                diseaseToDelete!!
                            )
                        )
                        scope.launch {
                            val undo = snackbarHostState.showSnackbar(
                                message = "Deleted disease ${diseaseToDelete?.name ?: ""}",
                                actionLabel = "UNDO"
                            )
                            if (undo == SnackbarResult.ActionPerformed) {
                                diseaseManagementViewModel.onEvent(DiseaseManagementEvent.UndoDelete)
                            }
                        }
                        dismissDeletionDialog()
                    }
                )
            },
            dismissButton = {
                DismissButton(
                    onDismiss = dismissDeletionDialog,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        )
    }
}


@Composable
fun DiseaseListItem(
    disease: Disease,
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
                    text = disease.name,
                    style = MaterialTheme.typography.bodyLarge
                )
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