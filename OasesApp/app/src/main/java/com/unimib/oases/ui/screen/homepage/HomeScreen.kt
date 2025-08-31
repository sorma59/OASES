package com.unimib.oases.ui.screen.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.ui.components.patients.PatientList
import com.unimib.oases.ui.components.patients.RecentlyReceivedPatientList
import com.unimib.oases.ui.components.scaffold.OasesTopAppBar
import com.unimib.oases.ui.components.scaffold.OasesTopAppBarType
import com.unimib.oases.ui.components.search.SearchBar
import com.unimib.oases.ui.components.util.BluetoothPermissionHandler
import com.unimib.oases.ui.components.util.NoPermissionMessage
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthState
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.util.ToastUtils
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    padding: PaddingValues,
    authViewModel: AuthViewModel,
    bluetoothManager: BluetoothCustomManager,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    val hasPermissions by bluetoothManager.hasPermissions.collectAsState()

    val authState = authViewModel.authState.observeAsState()

    val state by homeScreenViewModel.state.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val listState = remember { mutableStateListOf<String>() }


    val filteredItems = state.patients.filter { item ->
        item.publicId.contains(searchText, ignoreCase = true) || // Public id
        item.name.contains(searchText, ignoreCase = true)        // Name
    }


    BluetoothPermissionHandler(
        context = context,
        onPermissionGranted = {
            bluetoothManager.updatePermissions()
            bluetoothManager.initialize()
        }
    )

    val currentUser = remember { authViewModel.currentUser() }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated ->
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(0) { inclusive = true }
                }

            else -> Unit
        }
    }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { message ->
            ToastUtils.showToast(context, message)
        }
        homeScreenViewModel.onToastMessageShown()
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        if(homeScreenViewModel.state.value.patients.isEmpty()){
            homeScreenViewModel.getPatients()
        }
    }


    if (!hasPermissions) {
        NoPermissionMessage(context, bluetoothManager)
    } else {
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
                    Row {
                        Column(
                            Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                                .padding(
                                    top = padding.calculateTopPadding() + 10.dp,
                                    end = 16.dp,
                                    start = 10.dp,
                                    bottom = 16.dp
                                )
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Icon",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }



                                if (currentUser != null) {
                                    Text(
                                        text = currentUser.username,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(10.dp),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    // Buttons

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){


                        Row(modifier = Modifier) {

                            Button(

                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(0),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                border = BorderStroke(0.dp, Color.Transparent),
                                contentPadding = PaddingValues(0.dp),

                                onClick = {
                                    navController.navigate(Screen.PairDevice.route)
                                },

                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(10.dp),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        imageVector = Icons.Default.Bluetooth,
                                        contentDescription = "Bluetooth"
                                    )

                                    Text(
                                        "Bluetooth",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                        Row(modifier = Modifier) {

                            Button(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp),
                                shape = RoundedCornerShape(0),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                border = BorderStroke(0.dp, Color.Transparent),
                                contentPadding = PaddingValues(0.dp),

                                onClick = {
                                    authViewModel.signOut()
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
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "ContentDescriptions.Exit"
                                    )

                                    Text(
                                        "Logout",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = padding.calculateBottomPadding())
            ) {

                OasesTopAppBar(
                    title = "OASES",
                    type = OasesTopAppBarType.MENU,
                    showLogo = true,
                    onNavigationIconClick = {
                        scope.launch{
                            drawerState.open()
                        }
                    }
                )

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
                                    onQueryChange = {

                                        searchText = it },
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
                        RecentlyReceivedPatientList(state.receivedPatients, navController)
                        if(state.isLoading){
                            CustomCircularProgressIndicator()
                        }
                        else if (state.patients.isNotEmpty()) {
                            PatientList(
                                patients = filteredItems,
                                navController = navController,
                            )
                        }
                        if (state.errorMessage != null){
                            Text(state.errorMessage!!)
                        }

                    }
                    if (authViewModel.currentUser()?.role == Role.NURSE) { //TODO(Refactor this?)
                        FloatingActionButton(
                            onClick = { navController.navigate(Screen.RegistrationScreen.route) },
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
}
