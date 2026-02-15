package com.unimib.oases.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unimib.oases.ui.navigation.NavigationAnimation.Push
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
import com.unimib.oases.ui.screen.nurse_assessment.RegistrationScreen
import com.unimib.oases.ui.screen.nurse_assessment.demographics.DemographicsScreen
import com.unimib.oases.ui.screen.nurse_assessment.history.HistoryScreen
import com.unimib.oases.ui.screen.nurse_assessment.intake.InitialIntakeScreen
import com.unimib.oases.ui.screen.nurse_assessment.malnutrition_screening.MalnutritionScreeningScreen
import com.unimib.oases.ui.screen.nurse_assessment.triage.TriageScreen
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.VitalSignsSummary
import com.unimib.oases.ui.screen.nurse_assessment.vital_signs.form.VitalSignsForm
import com.unimib.oases.ui.screen.root.AppViewModel

@Composable
fun AppNavigation(
    startDestination: Route,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    val defaultAnimation = Push

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            defaultAnimation.enter
        },
        exitTransition = {
            defaultAnimation.exit
        },
        popEnterTransition = {
            defaultAnimation.popEnter
        },
        popExitTransition = {
            defaultAnimation.popExit
        }
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

        composable<Route.InitialIntake> {
            InitialIntakeScreen(appViewModel)
        }

        composable<Route.PatientRegistration> {
            RegistrationScreen(appViewModel, navController)
        }

        composable<Route.Demographics> {
            DemographicsScreen(appViewModel)
        }
        
        composable<Route.Triage>{
            TriageScreen(appViewModel)
        }

        composable<Route.VitalSigns> {
            VitalSignsSummary(appViewModel)
        }

        composable <Route.VitalSignsForm> {
            VitalSignsForm(appViewModel)
        }

        composable<Route.MalnutritionScreening> {
            MalnutritionScreeningScreen(appViewModel)
        }

        composable<Route.History> {
            HistoryScreen(appViewModel)
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

        composable<Route.MainComplaint>{
            MainComplaintScreen(appViewModel)
        }
    }
}