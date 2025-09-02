package com.unimib.oases.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    bluetoothCustomManager: BluetoothCustomManager,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(Screen.AdminDashboard.route) {
            AdminScreen(navController)
        }

        composable(Screen.VitalSignsManagement.route) {
            VitalSignManagementScreen()
        }

        composable(Screen.UserManagement.route) {
            UserManagementScreen ()
        }

        composable(Screen.DiseaseManagement.route) {
            DiseaseManagementScreen ()
        }

        composable(Screen.Home.route) {
            HomeScreen(authViewModel, bluetoothCustomManager, appViewModel)
        }

        composable(
            route = Screen.PatientRegistration.route + "?patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Home.route ->
                        slideIntoContainer(
                            SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Screen.Home.route ->
                        slideOutOfContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screen.Home.route ->
                        slideIntoContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Screen.Home.route ->
                        slideOutOfContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            }
        ) {
            RegistrationScreen(authViewModel, appViewModel)
        }

        composable(
            route = Screen.SendPatient.route + "/patientId={patientId}",
            arguments = listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            SendPatientViaBluetoothScreen(navController)
        }

        composable(Screen.PairDevice.route){
            PairNewDeviceScreen(navController)
        }

        composable(Screen.MedicalVisit.route) {
            MedicalVisitScreen(navController, authViewModel)
        }

        composable(
            route = Screen.PatientDashboard.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ) {
            PatientDashboardScreen(appViewModel)
        }

        composable(
            route = Screen.ViewPatientDetails.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            PatientDetailsScreen(appViewModel)
        }
    }
}