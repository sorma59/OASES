package com.unimib.oases.ui.components.scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.unimib.oases.R
import com.unimib.oases.ui.navigation.Route
import com.unimib.oases.ui.util.debounce
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

fun NavDestination?.getTitleAndType(): Pair<String, OasesTopAppBarType>? {
    return this?.route?.let {
        titlesAndTypes[it.substringBefore('/').substringBefore('?')]
    }
}

val titlesAndTypesByClass: Map<KClass<out Route>, Pair<String, OasesTopAppBarType>> = buildMap {
    put(Route.Home::class, "OASES" to OasesTopAppBarType.MENU)
    put(Route.InitialIntake::class, "New Visit" to OasesTopAppBarType.BACK)
    put(Route.AdminDashboard::class, "Admin Panel" to OasesTopAppBarType.MENU)
    put(Route.PatientRegistration::class, "Patient Registration" to OasesTopAppBarType.BACK)
    put(Route.VitalSignsForm::class, "Form" to OasesTopAppBarType.BACK)
    put(Route.Demographics::class, "Demographics" to OasesTopAppBarType.BACK)
    put(Route.Triage::class, "Triage" to OasesTopAppBarType.BACK)
    put(Route.VitalSigns::class, "Vital Signs" to OasesTopAppBarType.BACK)
    put(Route.MalnutritionScreening::class, "Malnutrition Screening" to OasesTopAppBarType.BACK)
    put(Route.History::class, "History" to OasesTopAppBarType.BACK)
    put(Route.SendPatient::class, "Send Patient" to OasesTopAppBarType.BACK)
    put(Route.MedicalVisit::class, "Medical Visit" to OasesTopAppBarType.BACK)
    put(Route.PatientDashboard::class, "Patient Dashboard" to OasesTopAppBarType.BACK)
    put(Route.ViewPatientDetails::class, "Patient Details" to OasesTopAppBarType.BACK)
    put(Route.MainComplaint::class, "Main Complaint" to OasesTopAppBarType.BACK)
    put(Route.DiseaseManagement::class, "Diseases Management" to OasesTopAppBarType.BACK)
    put(Route.UserManagement::class, "Users Management" to OasesTopAppBarType.BACK)
    put(Route.VitalSignsManagement::class, "Vital Signs Management" to OasesTopAppBarType.BACK)
    put(Route.RoomsManagement::class, "Rooms Management" to OasesTopAppBarType.BACK)
    put(Route.PairDevice::class, "Pair Device" to OasesTopAppBarType.BACK)
}

private val titlesAndTypes: Map<String, Pair<String, OasesTopAppBarType>> by lazy {
    titlesAndTypesByClass.mapKeys { (kclass, _) ->
        // Use the simple name of the class as the key (e.g., "Home", "Triage")
        kclass.qualifiedName ?: ""
    }
}

@Composable
fun OasesTopAppBar(
    navController: NavController,
    onBack: () -> Unit,
    onMenuClick: () -> Unit
){

    val entry by navController.currentBackStackEntryAsState()
    val config = entry?.destination?.getTitleAndType()

    config?.let { (title, type) ->
        when (type) {
            OasesTopAppBarType.MENU -> MenuTopAppBar(title, onMenuClick)
            OasesTopAppBarType.BACK -> BackTopAppBar(title, onBack)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopAppBar(
    title: String,
    onMenuClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(R.drawable.ic_launcher_round),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp)
                )
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
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
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    title: String,
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        navigationIcon = {
            IconButton(
                onClick = onBack.debounce()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Go Back"
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    )
}

@Serializable
enum class OasesTopAppBarType{
    MENU,
    BACK
}