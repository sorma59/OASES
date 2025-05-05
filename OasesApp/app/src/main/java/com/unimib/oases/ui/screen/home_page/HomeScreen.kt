package com.unimib.oases.ui.screen.home_page

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unimib.oases.ui.components.ActionIcon
import com.unimib.oases.ui.components.PatientUi
import com.unimib.oases.ui.components.SearchBar
import com.unimib.oases.ui.components.SwipeableItem

@Composable
fun HomeScreen(navController: NavController) {

    val patients = remember {
        mutableStateListOf(
            *(1..100).map {
                PatientUi(
                    id = it,
                    name = "Patient $it",
                    isOptionsRevealed = false
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
//                Column {
//                    Row (
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 12.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically){
//                        Text("OASES")
//                        IconButton(onClick = {}) {
//                            Icon(imageVector = Icons.Default.MoreVert,
//                                contentDescription = "More")
//                        }
//                    }

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
//                }
            }

            LazyColumn(contentPadding = PaddingValues(bottom = 30.dp, top = 10.dp)) {
                itemsIndexed(
                    items = patients,
                ) { index, patient ->
                    SwipeableItem(
                        modifier = Modifier.padding(5.dp),

                        isRevealed = patient.isOptionsRevealed,
                        onExpanded = {
                            patients[index] = patient.copy(isOptionsRevealed = true)
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
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.primary)
                        ) {

                            Column(modifier = Modifier.padding(10.dp)) {

                                Text(
                                    text = "Data ultima visita: ${"05/05/2025"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.surface,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = 0.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    maxLines = 1
                                )

                                Text(
                                    text = "Paziente ${patient.id}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.surface,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    maxLines = 1
                                )

                                Text(
                                    text = "Stato: 🟡",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.surface,
                                    fontWeight = FontWeight.Normal,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1

                                )
                            }
                        }

                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate("registration_screen") },
            modifier = Modifier.padding(bottom = 30.dp)

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