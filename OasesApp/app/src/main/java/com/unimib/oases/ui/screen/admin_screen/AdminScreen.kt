package com.unimib.oases.ui.screen.admin_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import com.unimib.oases.ui.navigation.Screen
import com.unimib.oases.ui.screen.login.AuthState
import com.unimib.oases.ui.screen.login.AuthViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    padding : PaddingValues,
    authViewModel: AuthViewModel,
    adminViewModel: AdminViewModel = hiltViewModel(),
) {

    val state by adminViewModel.state.collectAsState()

    val authState = authViewModel.authState.observeAsState()


    val snackbarHostState =
        remember { SnackbarHostState() } // for hosting snackbars, if I delete a intem I get a snackbar to undo the item

    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        adminViewModel.getUsers()
    }


    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated ->
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(0) { inclusive = true }
                }
            else -> Unit
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
                        "Admin Panel",
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
                    authViewModel.signout()
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
                .padding(horizontal = 20.dp)
                .padding(bottom = padding.calculateBottomPadding()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "User registration:",
                    style = MaterialTheme.typography.titleLarge
                )
            }


            OutlinedTextField(
                value = state.user.username,
                onValueChange = { adminViewModel.onEvent(AdminEvent.EnteredUsername(it)) },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.user.pwHash,
                onValueChange = { adminViewModel.onEvent(AdminEvent.EnteredPassword(it)) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "PASSWORD")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))


            // Role selection radio buttons
            Text(
                text = "Select Role:",
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Role.entries.forEach { roleOption ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (roleOption == state.user.role),
                                onClick = { adminViewModel.onEvent(AdminEvent.SelectedRole(roleOption)) }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (roleOption == state.user.role),
                            onClick = { adminViewModel.onEvent(AdminEvent.SelectedRole(roleOption)) }
                        )
                        Text(
                            text = roleOption.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }


                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {

                        if (state.user.username.isBlank() || state.user.pwHash.isBlank()) {
                            state.error = "Username and password cannot be empty!"
                            return@Button
                        }

                        adminViewModel.onEvent(AdminEvent.SaveUser)

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create User")
                }

                Spacer(modifier = Modifier.height(16.dp))

                state.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.message?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Registered Users (${state.users.size})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )


                if (state.isLoading) {
                    CustomCircularProgressIndicator()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(state.users.size) { i ->
                            val user = state.users[i]
                            UserListItem(
                                user = user,
                                onDelete = {
                                    adminViewModel.onEvent(AdminEvent.Delete(user))
                                    scope.launch {
                                        val undo = snackbarHostState.showSnackbar(
                                            message = "Deleted user ${user.username}",
                                            actionLabel = "UNDO"
                                        )
                                        if (undo == SnackbarResult.ActionPerformed) {
                                            adminViewModel.onEvent(AdminEvent.UndoDelete)
                                        }
                                    }
                                },
                                onClick = {
                                    adminViewModel.onEvent(AdminEvent.Click(user))
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun UserListItem(
    user: User,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (user.username.isNotEmpty()) {
                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete user",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}