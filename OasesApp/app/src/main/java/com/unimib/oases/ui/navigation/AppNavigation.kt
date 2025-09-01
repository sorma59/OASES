package com.unimib.oases.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.ui.screen.bluetooth.pairing.PairNewDeviceScreen
import com.unimib.oases.ui.screen.bluetooth.sending.SendPatientViaBluetoothScreen
import com.unimib.oases.ui.screen.dashboard.admin.AdminScreen
import com.unimib.oases.ui.screen.dashboard.admin.disease.DiseaseManagementScreen
import com.unimib.oases.ui.screen.dashboard.admin.user.UserManagementScreen
import com.unimib.oases.ui.screen.dashboard.admin.vitalsigns.VitalSignManagementScreen
import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardScreen
import com.unimib.oases.ui.screen.dashboard.patient.view.PatientDetailsScreen
import com.unimib.oases.ui.screen.homepage.HomeScreen
import com.unimib.oases.ui.screen.login.AuthState
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.login.LoginScreen
import com.unimib.oases.ui.screen.medical_visit.MedicalVisitScreen
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreen
import com.unimib.oases.ui.util.ToastUtils

/**
 * Navigates to the login screen clearing the whole backstack beforehand
 */

fun NavController.navigateToLogin(){
    clearBackStackAndNavigate(Screen.LoginScreen.route)
}

fun NavController.clearBackStackAndNavigate(route: String){
    navigate(route) {
        popUpTo(0){
            inclusive = true
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    padding: PaddingValues,
    bluetoothCustomManager: BluetoothCustomManager
){

    val authViewModel: AuthViewModel = hiltViewModel()

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.Authenticated -> {
                when (state.user.role) {
                    Role.ADMIN -> navController.clearBackStackAndNavigate(Screen.AdminScreen.route)
                    Role.DOCTOR, Role.NURSE -> navController.clearBackStackAndNavigate(Screen.HomeScreen.route)
                }
            }
            AuthState.Unauthenticated -> {
                navController.navigateToLogin()
            }
            is AuthState.Error -> {
                ToastUtils.showToast(navController.context, (authState.value as AuthState.Error).message)
            }
            else -> Unit
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
    ) {

        composable(Screen.AdminScreen.route) {
            AdminScreen(navController, padding, authViewModel)
        }

        composable(Screen.VitalSignsManagementScreen.route) {
            VitalSignManagementScreen(navController, padding)
        }

        composable(Screen.UserManagementScreen.route) {
            UserManagementScreen (navController, padding)
        }

        composable(Screen.DiseaseManagementScreen.route) {
            DiseaseManagementScreen (navController, padding)
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, padding, authViewModel)
        }

        composable(Screen.HomeScreen.route,
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.LoginScreen.route ->
                        slideOutOfContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Screen.LoginScreen.route ->
                        slideOutOfContainer(
                            SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            }
        ) {
            HomeScreen(navController, padding, authViewModel, bluetoothCustomManager)
        }

        composable(
            route = Screen.RegistrationScreen.route + "?patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.HomeScreen.route ->
                        slideIntoContainer(
                            SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.HomeScreen.route ->
                        slideOutOfContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screen.HomeScreen.route ->
                        slideIntoContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Screen.HomeScreen.route ->
                        slideOutOfContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            }
        ) {
            RegistrationScreen(navController, padding, authViewModel)
        }

        composable(
            route = Screen.SendPatient.route + "/patientId={patientId}",
            arguments = listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            SendPatientViaBluetoothScreen(
                navController = navController,
                padding = padding
            )
        }

        composable(Screen.PairDevice.route){
            PairNewDeviceScreen(navController, padding)
        }

        composable(Screen.MedicalVisitScreen.route) {
            MedicalVisitScreen(navController, authViewModel, padding)
        }

        composable(
            route = Screen.PatientDashboardScreen.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ) {
            PatientDashboardScreen(navController, authViewModel, padding)
        }

        composable(
            route = Screen.ViewPatientDetailsScreen.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            PatientDetailsScreen(navController, authViewModel, padding)
        }
    }
}