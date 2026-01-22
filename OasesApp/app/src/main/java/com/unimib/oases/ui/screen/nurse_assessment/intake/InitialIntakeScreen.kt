package com.unimib.oases.ui.screen.nurse_assessment.intake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.seanproctor.datatable.DataColumn
import com.seanproctor.datatable.material3.DataTable
import com.unimib.oases.domain.model.Patient
import com.unimib.oases.ui.components.search.SearchBar
import com.unimib.oases.ui.components.util.CenteredText
import com.unimib.oases.ui.components.util.TitleText
import com.unimib.oases.ui.components.util.button.BottomButtons
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.effect.HandleUiEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.util.StringFormatHelper
import com.unimib.oases.util.StringFormatHelper.formatDate

@Composable
fun InitialIntakeScreen(
    appViewModel: AppViewModel
){
    val viewModel: InitialIntakeViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    HandleUiEvents(viewModel.uiEvents, appViewModel)

    LoadingOverlay(state.isReturn && state.isLoading)

    InitialIntakeContent(state, viewModel::onEvent, Modifier.fillMaxSize())
}

@Composable
private fun InitialIntakeContent(
    state: InitialIntakeState,
    onEvent: (InitialIntakeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isReturn)
        PatientsListContent(state, onEvent, modifier)
    else
        NewOrReturnContent(onEvent, modifier)
}

@Composable
private fun PatientsListContent(
    state: InitialIntakeState,
    onEvent: (InitialIntakeEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val listState = remember { mutableStateListOf<String>() }

    val onQueryChange = { text: String ->
        searchText = text
    }

    val onSearch = {
        listState.add(searchText)
        active = false
    }

    val filteredItems = state.patients.filter { item ->
        item.publicId.contains(searchText, ignoreCase = true) || // Public id
                item.name.contains(searchText, ignoreCase = true)        // Name
    }

    val colorEven = MaterialTheme.colorScheme.surfaceBright
    val colorOdd = MaterialTheme.colorScheme.surfaceDim.copy(0.5f)

    fun getAgeString(patient: Patient) = StringFormatHelper.getAgeWithSuffix(patient.ageInMonths)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier.padding(16.dp),
    ) {


        TitleText("Tap a patient to start a new visit")

        SearchBar(
            query = searchText,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = { active = it },
            searchHistory = listState,
            onHistoryItemClick = {
                onQueryChange(it)
                onSearch()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        if (filteredItems.isNotEmpty()) {
            DataTable(
                headerBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth(),
                columns = listOf(
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Public ID")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Name")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Age")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Gender")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Village")
                    },
                    DataColumn(Alignment.Center) {
                        Text(color = MaterialTheme.colorScheme.surface, text = "Last time here")
                    }
                )
            ) {

                // Iterate over the list of patients
                filteredItems.forEachIndexed {index, patient ->
                    row {
                        backgroundColor = if (index % 2 == 0) colorEven else colorOdd
                        // Handle row click
                        onClick = {
                            onEvent(InitialIntakeEvent.PatientClicked(patient.id))
                        }

                        cell {
                            Text(patient.publicId, textAlign = TextAlign.Center)
                        }

                        cell {
                            Text(patient.name, textAlign = TextAlign.Center)
                        }

                        cell {
                            Text(getAgeString(patient.patient), textAlign = TextAlign.Center)
                        }

                        cell {
                            Text(patient.sex.displayName)
                        }

                        cell {
                            Text(patient.village)
                        }

                        cell {
                            Text(formatDate(patient.lastVisitDate))
                        }
                    }
                }
            }
        } else
            CenteredText("No patients found", Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun NewOrReturnContent(
    onEvent: (InitialIntakeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        CenteredText(
            "Add a patient",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        CenteredText(
            "Has the patient been here before?",
            fontSize = 20.sp
        )

        Spacer(Modifier.height(128.dp))

        BottomButtons(
            onCancel = { onEvent(InitialIntakeEvent.NewButtonClicked) },
            onConfirm = { onEvent(InitialIntakeEvent.ReturnButtonClicked) },
            cancelButtonText = "No",
            confirmButtonText = "Yes"
        )

    }
}