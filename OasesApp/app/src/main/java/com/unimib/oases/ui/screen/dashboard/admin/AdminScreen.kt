package com.unimib.oases.ui.screen.dashboard.admin

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unimib.oases.ui.components.text.AutoResizedText
import com.unimib.oases.ui.navigation.Screen

@Composable
fun AdminScreen(
    navController: NavController
) {

    val configuration = LocalConfiguration.current

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                val itemSize = when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> DpSize(maxWidth * 1f, maxHeight * 0.20f)
                    Configuration.ORIENTATION_LANDSCAPE -> DpSize(maxWidth * 0.4f, maxHeight * 0.3f)
                    else -> DpSize(maxWidth * 0.3f, maxHeight * 0.4f)
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(2.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    item {
                        Button(
                            onClick = { navController.navigate(Screen.UserManagement.route) },
                            shape = MaterialTheme.shapes.extraSmall,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(itemSize)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "",
                                        modifier = Modifier.size(itemSize/2)
                                    )
                                }

                                Row {
                                    AutoResizedText(text = "Users", style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { navController.navigate(Screen.DiseaseManagement.route) },
                            shape = MaterialTheme.shapes.extraSmall,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(itemSize)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.MedicalInformation,
                                        contentDescription = "",
                                        modifier = Modifier.size(itemSize/2)
                                    )
                                }

                                Row {
                                    AutoResizedText(text = "Diseases", style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { navController.navigate(Screen.VitalSignsManagement.route) },
                            shape = MaterialTheme.shapes.extraSmall,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(itemSize)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Bloodtype,
                                        contentDescription = "",
                                        modifier = Modifier.size(itemSize/2)
                                    )
                                }

                                Row {
                                    AutoResizedText(text = "Vital Signs", style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}