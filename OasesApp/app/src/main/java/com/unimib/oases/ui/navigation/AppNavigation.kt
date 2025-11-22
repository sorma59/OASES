package com.unimib.oases.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.unimib.oases.ui.screen.nurse_assessment.demographics.DemographicsScreen
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningScreen
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageScreen
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsScreen
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun AppNavigation(
    startDestination: Route,
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

        composable<Route.AdminDashboard> {
            AdminScreen(navController)
        }

        composable<Route.VitalSignsManagement> {
            VitalSignManagementScreen()
        }

        composable<Route.UserManagement> {
            UserManagementScreen()
        }

        composable<Route.DiseaseManagement> {
            DiseaseManagementScreen()
        }

        composable<Route.RoomsManagement>{
            RoomsManagementScreen()
        }

        composable<Route.Home> {
            HomeScreen(authViewModel, appViewModel)
        }

        composable<Route.PatientRegistration>(
            enterTransition = {
                slideIntoContainer(
                    SlideDirection.Left,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    SlideDirection.Right,
                    animationSpec = tween(400)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    SlideDirection.Right,
                    animationSpec = tween(400)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    SlideDirection.Right,
                    animationSpec = tween(400)
                )
            }
        ) {
            RegistrationScreen(appViewModel, navController)
        }

        composable<Route.Demographics> {
            DemographicsScreen(appViewModel)
        }
        
        composable<Route.Triage>{
            TriageScreen(appViewModel)
        }

        composable<Route.VitalSigns> {
            VitalSignsScreen(appViewModel)
        }

        composable<Route.MalnutritionScreening> {
            MalnutritionScreeningScreen(appViewModel)
        }

        composable<Route.SendPatient> {
            SendPatientViaBluetoothScreen(navController)
        }

        composable<Route.PairDevice>{
            PairNewDeviceScreen(navController)
        }

        composable<Route.MedicalVisit> {
            MedicalVisitScreen(appViewModel)
        }

        composable<Route.PatientDashboard> {
            PatientDashboardScreen(appViewModel)
        }

        composable<Route.ViewPatientDetails> {
            PatientDetailsScreen(appViewModel)
        }

        composable<Route.PastMedicalHistory>{
            PastHistoryScreen(appViewModel)
        }

        composable<Route.MainComplaint>{
            MainComplaintScreen(appViewModel)
        }
    }
}