package com.unimib.oases.ui.screen.home_page

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.unimib.oases.R
import com.unimib.oases.data.model.Role
import com.unimib.oases.ui.components.ActionIcon
import com.unimib.oases.ui.components.SearchBar
import com.unimib.oases.ui.home_page.components.card.PatientCard
import com.unimib.oases.ui.home_page.components.card.PatientUi
import com.unimib.oases.ui.screen.login.AuthState
import com.unimib.oases.ui.screen.login.AuthViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
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
                    state = arrayOf("G", "R", "Y").random()
                )
            }.toTypedArray()
        )
    }

    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel();
    val authState by authViewModel.authState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxHeight(),
        drawerState = drawerState,
        drawerContent = {


            ModalDrawerSheet(
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight(),
                (RoundedCornerShape(0.dp)),
                windowInsets = WindowInsets(top = 0),
                drawerContainerColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Row() {

                    Column(
                        Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth()
                            .padding(top = 80.dp, end = 16.dp, start = 16.dp, bottom = 16.dp)
                    ) {


                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Icon",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(Modifier.padding(10.dp))

                        Text(
                            text = "utente",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }


                }

                HorizontalDivider()


                Row(modifier = Modifier, horizontalArrangement = Arrangement.Start) {

                    Button(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        shape = RoundedCornerShape(0),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
                        border = BorderStroke(0.dp, Color.Transparent),
                        contentPadding = PaddingValues(0.dp),

                        onClick = {
                            // TODO signout
                        },

                        ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(10.dp),
                                tint = Color.Black,
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "ContentDescriptions.Exit"
                            )

                            Text(
                                "Logout",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.background
                            )

                        }


                    }
                }

            }


        }) {
        Scaffold(

            topBar = {

                CenterAlignedTopAppBar(

                    title = {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){

                                Image(
                                    painter = painterResource(R.drawable.ic_launcher_round),
                                    contentDescription = "Icon",
                                    modifier = Modifier.size(50.dp).padding(5.dp)
                                )

                                Text("OASES", fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis)
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
                            scope.launch { drawerState.open() } // open sorting menu
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "MENU"
                            )
                        }
                    },
                    actions = {},
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                )
            },
        )


        { innerPadding ->

            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.weight(1f),
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
                                query = "",
                                onQueryChange = { },
                                onSearch = { },
                                active = false,
                                onActiveChange = { },
                                searchHistory = emptyList()
                            ) { }
                        }
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
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
                                            patients[index] =
                                                patient.copy(isOptionsRevealed = false)
                                            Toast.makeText(
                                                context,
                                                "Patients ${patient.id} was shared.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        backgroundColor = Color.Gray,
                                        icon = Icons.Default.Bluetooth,

                                        modifier = Modifier.fillMaxHeight()
                                    )
                                },
                                onCardClick = {
                                    if(authState is AuthState.Authenticated && (authState as AuthState.Authenticated).user.role == Role.Nurse)
                                        navController.navigate("registration_screen")
                                    else
                                        navController.navigate("medical_visit_screen")
                                },
                            )
                        }
                    }
                }
                if(authState is AuthState.Authenticated && (authState as AuthState.Authenticated).user.role == Role.Nurse){
                    FloatingActionButton(
                        onClick = { navController.navigate("registration_screen") },
                        modifier = Modifier.padding(30.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.surface,
                            imageVector = Icons.Default.Add,
                            contentDescription = "History",
                        )

                    }
                }
            }
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