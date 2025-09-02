package com.unimib.oases.ui.components.scaffold

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthViewModel

@Composable
fun OasesDrawer(
    drawerState: DrawerState,
    padding: PaddingValues,
    authViewModel: AuthViewModel,
    navController: NavController,
    content: @Composable () -> Unit
){
    val currentUser = remember { authViewModel.currentUser }

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

                            currentUser?.let {
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

                HorizontalDivider(color = MaterialTheme.colorScheme.primary)

                // Buttons

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {


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
        },
        content = content
    )
}