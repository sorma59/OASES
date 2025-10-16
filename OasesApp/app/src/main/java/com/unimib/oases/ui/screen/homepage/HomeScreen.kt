package com.unimib.oases.ui.screen.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.unimib.oases.ui.components.patients.PatientList
import com.unimib.oases.ui.components.search.SearchBar
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.root.AppViewModel
import com.unimib.oases.ui.util.ToastUtils


@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val state by homeScreenViewModel.state.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val listState = remember { mutableStateListOf<String>() }

    val onPatientItemClick = { patientId: String ->
        homeScreenViewModel.onEvent(HomeScreenEvent.PatientItemClicked(patientId))
    }

    val userRole by authViewModel.userRole.collectAsState()

    val shouldShowAddPatientButton = userRole == Role.NURSE

    val filteredItems = state.patients.filter { item ->
        item.publicId.contains(searchText, ignoreCase = true) || // Public id
        item.name.contains(searchText, ignoreCase = true)        // Name
    }

    LaunchedEffect(Unit) {
        homeScreenViewModel.navigationEvents.collect{
            appViewModel.onNavEvent(it)
        }
    }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            ToastUtils.showToast(context, message)
        }
        homeScreenViewModel.onToastMessageShown()
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
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 12.dp)
                    ) {
                        SearchBar(
                            query = searchText,
                            onQueryChange = { searchText = it },
                            onSearch = {
                                listState.add(searchText)
                                active = false
                            },
                            active = active,
                            onActiveChange = { active = it },
                            searchHistory = listState,
                            onHistoryItemClick = { searchText = it })
                    }
                }
                if(state.isLoading){
                    CustomCircularProgressIndicator()
                }
                else if (state.patients.isNotEmpty()) {
                    PatientList(
                        patients = filteredItems,
                        onItemClick = onPatientItemClick
                    )
                }
                if (state.errorMessage != null){
                    Text(state.errorMessage!!)
                }

            }
            if (shouldShowAddPatientButton) { //TODO(Refactor this?)
                FloatingActionButton(
                    onClick = { homeScreenViewModel.onEvent(HomeScreenEvent.AddButtonClicked) },
                    modifier = Modifier.padding(30.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.surface,
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add a patient",
                    )
                }
            }
        }
    }
}
