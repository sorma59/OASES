package com.unimib.oases.ui.screen.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.domain.model.PatientAndVisitIds
import com.unimib.oases.ui.components.patients.PatientsWithVisitInfoList
import com.unimib.oases.ui.components.search.SearchBar
import com.unimib.oases.ui.components.util.effect.HandleNavigationEvents
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.ToastUtils

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel,
) {

    val viewModel: HomeScreenViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    HandleNavigationEvents(viewModel.navigationEvents, appViewModel)

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            ToastUtils.showToast(context, message)
        }
        viewModel.onToastMessageShown()
    }

    LoadingOverlay(state.isLoading)

    HomeContent(state, viewModel::onEvent, authViewModel)
}

@Composable
private fun HomeContent(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit,
    authViewModel: AuthViewModel
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

    val onPatientItemClick = { ids: PatientAndVisitIds ->
        onEvent(HomeScreenEvent.PatientItemClicked(ids))
    }

    val userRole by authViewModel.userRole.collectAsState()

    val shouldShowAddPatientButton = userRole == Role.NURSE

    val filteredItems = state.patientsWithVisitInfo.filter { item ->
        item.patient.publicId.contains(searchText, ignoreCase = true) || // Public id
        item.patient.name.contains(searchText, ignoreCase = true)        // Name
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
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
                        }
                    )
                }

                if (state.patientsWithVisitInfo.isNotEmpty()) {
                    PatientsWithVisitInfoList(
                        patientsWithVisitInfo = filteredItems,
                        onItemClick = onPatientItemClick
                    )
                }
                state.error?.let {
                    Text(text = it)
                }

            }
            if (shouldShowAddPatientButton) { //TODO(Refactor this?)
                FloatingActionButton(
                    onClick = { onEvent(HomeScreenEvent.AddButtonClicked) },
                    modifier = Modifier.padding(30.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add a patient",
                    )
                }

                Spacer(Modifier.height(64.dp))
            }
        }
    }
}
