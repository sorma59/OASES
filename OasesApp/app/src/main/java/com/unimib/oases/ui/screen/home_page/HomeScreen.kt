package com.unimib.oases.ui.screen.home_page

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unimib.oases.ui.components.ActionIcon
import com.unimib.oases.ui.components.SearchBar
import com.unimib.oases.ui.home_page.components.PatientCard
import com.unimib.oases.ui.home_page.components.PatientUi

@Composable
fun HomeScreen(navController: NavController) {

    val patients = remember {
        mutableStateListOf(
            *(1..100).map {
                PatientUi(
                    id = it,
                    name = "Patient $it",
                    isOptionsRevealed = false,
                    lastVisit = "05/05/2025",
                    state = "🟡"
                )
            }.toTypedArray()
        )
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 12.dp)
                ) {
                    SearchBar(
                        query = "",
                        onQueryChange = { },
                        onSearch = { },
                        active = false,
                        onActiveChange = { },
                        searchHistory = emptyList()
                    ) { }
                }
            }

            LazyColumn(  verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
                itemsIndexed(
                    items = patients,
                ) { index, patient ->
                    PatientCard(
                        patient = patient,
                        isRevealed = patient.isOptionsRevealed,
                        onExpanded = {
                            patients.replaceAll { p ->
                                p.copy(isOptionsRevealed = p.id == patient.id)
                            }
                        },
                        onCollapsed = {
                            patients[index] = patient.copy(isOptionsRevealed = false)
                        },
                        actions = {
                            ActionIcon(
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Patient ${patient.id} was deleted.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    patients.remove(patient)
                                },
                                backgroundColor = MaterialTheme.colorScheme.error,
                                icon = Icons.Default.Delete,
                                modifier = Modifier.fillMaxHeight()
                            )
                            ActionIcon(
                                onClick = {
                                    patients[index] = patient.copy(isOptionsRevealed = false)
                                    Toast.makeText(
                                        context,
                                        "Patients ${patient.id} was shared.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                icon = Icons.Default.Bluetooth,
                                modifier = Modifier.fillMaxHeight()
                            )
                        },
                        onCardClick = {
                            navController.navigate("registration_screen")
                        },
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate("registration_screen") },
            modifier = Modifier.padding(30.dp)

        ) {
            Icon(Icons.Default.Add, "Add")
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    OasesTheme {
//        HomeScreen(navController = rememberNavController())
//    }
//}