package com.unimib.oases.ui.screen.medical_visit


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthViewModel
import com.unimib.oases.ui.screen.medical_visit.info.PatientInfoScreen
import com.unimib.oases.ui.screen.medical_visit.past_medical_history.PastHistoryScreen
import com.unimib.oases.ui.screen.medical_visit.visit.VisitScreen
import com.unimib.oases.ui.screen.medical_visit.visit_history.VisitHistoryScreen


@Composable
fun MedicalVisitScreen(navController: NavController, authViewModel: AuthViewModel) {
    MedicalVisitTabNavigation(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalVisitTabNavigation(
    navController: NavController,
    authViewModel: AuthViewModel? = null
) {
    val tabs = listOf(
        "Dati anagrafici",
        "History",
        "Past Medical History",
        "Medical Visit"
    )
    var currentIndex by remember { mutableIntStateOf(0) }

    val nextButtonText = remember(currentIndex) {
        if (currentIndex == tabs.lastIndex) {
            "Submit"
        } else {
            "Next"
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
                        "Medical Visit",
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
                    navController.popBackStack()
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
                .fillMaxSize()
                .weight(1f) // Take up remaining vertical space
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tabs[currentIndex],
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Content area takes up the rest
                contentAlignment = Alignment.Center
            ) {
                when (currentIndex) {
                    0 -> PatientInfoScreen()
                    1 -> VisitHistoryScreen()
                    2 -> PastHistoryScreen()
                    3 -> VisitScreen()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (currentIndex > 0) {
                        OutlinedButton(onClick = { currentIndex-- }) {
                            Text("Back")
                        }
                    } else {
                        OutlinedButton(onClick = { navController.popBackStack() }) {
                            Text("Back")
                        }
                    }
                }

                Column {
                    Button(
                        onClick = {
                            if (currentIndex < tabs.lastIndex) {
                                currentIndex++
                            } else {
                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Text(text = nextButtonText)
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun MedicalVisitScreenPreview() {
//    MedicalVisitTabNavigation(navController = rememberNavController())
//}