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
import com.unimib.oases.ui.navigation.Screen.AdminDashboard
import com.unimib.oases.ui.navigation.Screen.DiseaseManagement
import com.unimib.oases.ui.navigation.Screen.Home
import com.unimib.oases.ui.navigation.Screen.MainComplaintScreen
import com.unimib.oases.ui.navigation.Screen.MedicalVisit
import com.unimib.oases.ui.navigation.Screen.PairDevice
import com.unimib.oases.ui.navigation.Screen.PatientDashboard
import com.unimib.oases.ui.navigation.Screen.PatientRegistration
import com.unimib.oases.ui.navigation.Screen.SendPatient
import com.unimib.oases.ui.navigation.Screen.UserManagement
import com.unimib.oases.ui.navigation.Screen.ViewPatientDetails
import com.unimib.oases.ui.navigation.Screen.VitalSignsManagement
import com.unimib.oases.ui.navigation.Screen.RoomsManagement
import com.unimib.oases.ui.screen.bluetooth.pairing.PairNewDeviceScreen
import com.unimib.oases.ui.screen.bluetooth.sending.SendPatientViaBluetoothScreen
import com.unimib.oases.ui.screen.dashboard.admin.AdminScreen
import com.unimib.oases.ui.screen.dashboard.admin.disease.DiseaseManagementScreen
import com.unimib.oases.ui.screen.dashboard.admin.rooms.RoomsManagementScreen
import com.unimib.oases.ui.screen.dashboard.admin.user.UserManagementScreen
import com.unimib.oases.ui.screen.dashboard.admin.vitalsigns.VitalSignManagementScreen
import com.unimib.oases.ui.screen.dashboard.patient.PatientDashboardScreen
import com.unimib.oases.ui.screen.dashboard.patient.view.PatientDetailsScreen
import com.unimib.oases.ui.screen.homepage.HomeScreen
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.medical_visit.MedicalVisitScreen
import com.unimib.oases.ui.screen.medical_visit.maincomplaint.MainComplaintScreen
import com.unimib.oases.ui.screen.medical_visit.pmh.PastHistoryScreen
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreen
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun AppNavigation(
    startDestination: String,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(AdminDashboard.route) {
            AdminScreen(navController)
        }

        composable(VitalSignsManagement.route) {
            VitalSignManagementScreen()
        }

        composable(UserManagement.route) {
            UserManagementScreen ()
        }

        composable(DiseaseManagement.route) {
            DiseaseManagementScreen ()
        }

        composable(RoomsManagement.route){
            RoomsManagementScreen()
        }

        composable(Home.route) {
            HomeScreen(authViewModel, appViewModel)
        }

        composable(
            route = PatientRegistration.route + "?patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            ),
            enterTransition = {
                when (initialState.destination.route) {
                    Home.route ->
                        slideIntoContainer(
                            SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    Home.route ->
                        slideOutOfContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Home.route ->
                        slideIntoContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    Home.route ->
                        slideOutOfContainer(
                            SlideDirection.Right,
                            animationSpec = tween(400)
                        )
                    else -> null
                }
            }
        ) {
            RegistrationScreen(appViewModel)
        }

        composable(
            route = SendPatient.route + "/patientId={patientId}",
            arguments = listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            SendPatientViaBluetoothScreen(navController)
        }

        composable(PairDevice.route){
            PairNewDeviceScreen(navController)
        }

        composable(MedicalVisit.route + "/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")!!
            MedicalVisitScreen(patientId, appViewModel)
        }

        composable(
            route = PatientDashboard.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ) {
            PatientDashboardScreen(appViewModel)
        }

        composable(
            route = ViewPatientDetails.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            PatientDetailsScreen(appViewModel)
        }

        composable(
            route = Screen.PastMedicalHistoryScreen.route + "/patientId={patientId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                }
            )
        ){
            PastHistoryScreen(appViewModel)
        }

        composable(
            route = MainComplaintScreen.route + "/patientId={patientId}" + "/complaintId={complaintId}",
            arguments =  listOf(
                navArgument("patientId"){
                    type = NavType.StringType
                },
                navArgument("complaintId"){
                    type = NavType.StringType
                }
            )
        ){
            MainComplaintScreen()
        }
    }
}