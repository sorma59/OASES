package com.unimib.oases.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.unimib.oases.data.bluetooth.BluetoothCustomManager
import com.unimib.oases.ui.screen.bluetooth.pairing.PairNewDeviceScreen
import com.unimib.oases.ui.screen.bluetooth.sending.SendPatientViaBluetoothScreen
import com.unimib.oases.ui.screen.dashboard.admin.AdminScreen
import com.unimib.oases.ui.screen.dashboard.admin.disease.DiseaseManagementScreen
import com.unimib.oases.ui.screen.dashboard.admin.user.UserManagementScreen
import com.unimib.oases.ui.screen.dashboard.admin.vitalsigns.VitalSignManagementScreen
import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardScreen
import com.unimib.oases.ui.screen.dashboard.patient.view.PatientDetailsScreen
import com.unimib.oases.ui.screen.homepage.HomeScreen
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.medical_visit.MedicalVisitScreen
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreen
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun AppNavigation(
    startDestination: String,
    navController: NavHostController,
    padding: PaddingValues,
    bluetoothCustomManager: BluetoothCustomManager,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
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

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, padding, authViewModel, bluetoothCustomManager, appViewModel)
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
            RegistrationScreen(navController, padding, authViewModel, appViewModel)
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
            PatientDashboardScreen(padding, appViewModel)
        }

        composable(
            route = Screen.ViewPatientDetailsScreen.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            PatientDetailsScreen(padding, appViewModel)
        }
    }
}