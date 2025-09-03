package com.unimib.oases.ui.components.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unimib.oases.data.local.model.User
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthViewModel
import kotlinx.coroutines.launch


@Composable
fun OasesDrawer(
    drawerState: DrawerState,
    authViewModel: AuthViewModel,
    navController: NavController,
    content: @Composable () -> Unit
){
    val currentUser = remember { authViewModel.currentUser }

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxHeight(),
        drawerState = drawerState,
        drawerContent = {

            val screenWidthDp = LocalWindowInfo.current.containerSize.width

            val drawerWidth = when {
                screenWidthDp < 600 -> 280.dp // phone
                screenWidthDp < 840 -> 360.dp // small tablet
                else -> 400.dp               // large tablet
            }

            ModalDrawerSheet(
                drawerState = drawerState,
                modifier = Modifier
                    .width(drawerWidth)
                    .systemBarsPadding()
                    .fillMaxHeight(),
                windowInsets = WindowInsets(top = 0),
                drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ) {

                UserRow(currentUser, authViewModel)

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    thickness = 0.5.dp
                )

                Buttons(drawerState, navController)
            }
        },
        content = content
    )
}

@Composable
private fun UserRow(currentUser: User?, authViewModel: AuthViewModel) {
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ){
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        IconButton(
            onClick = authViewModel::signOut
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Default.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun Buttons(
    drawerState: DrawerState,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        DrawerButton(
            drawerState = drawerState,
            icon = Icons.Default.Bluetooth,
            label = "Bluetooth",
            contentDescription = "Bluetooth",
            onClick = { navController.navigate(Screen.PairDevice.route) }
        )
    }
}

@Composable
fun DrawerButton(
    drawerState: DrawerState,
    icon: ImageVector,
    label: String,
    contentDescription: String,
    onClick: () -> Unit
){
    val scope = rememberCoroutineScope()

    Button(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = PaddingValues(0.dp),   // Override the built-in padding
        onClick = {
            scope.launch {
                drawerState.close()
                onClick()
            }
        }
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                imageVector = icon,
                contentDescription = contentDescription
            )

            Text(
                label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}