package com.unimib.oases.ui.screen.admin_screen

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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unimib.oases.ui.components.text.AutoResizedText
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthState
import com.unimib.oases.ui.screen.login.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    padding: PaddingValues,
    authViewModel: AuthViewModel,
) {

    val configuration = LocalConfiguration.current
//    val gridColumns = when (configuration.orientation) {
//        Configuration.ORIENTATION_PORTRAIT -> 3
//        Configuration.ORIENTATION_LANDSCAPE -> 3
//        else -> 3
//    }


    val authState = authViewModel.authState.observeAsState()


    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated ->
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(0) { inclusive = true }
                }

            else -> Unit
        }
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
                    authViewModel.signOut()
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
                .padding(horizontal = 10.dp)
                .fillMaxSize()

                .padding(bottom = padding.calculateBottomPadding()),
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
                            onClick = { navController.navigate(Screen.UserManagementScreen.route) },
                            shape = MaterialTheme.shapes.extraSmall,
                            modifier = Modifier.padding(5.dp).size(itemSize)
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
                            onClick = { navController.navigate(Screen.DiseaseManagementScreen.route) },
                            shape = MaterialTheme.shapes.extraSmall,
                            modifier = Modifier.padding(5.dp).size(itemSize)
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
                            onClick = { navController.navigate(Screen.VitalSignsManagementScreen.route) },
                            shape = MaterialTheme.shapes.extraSmall,
                            modifier = Modifier.padding(5.dp).size(itemSize)
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