package com.unimib.oases.ui.screen.admin_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.data.model.Role
import com.unimib.oases.data.model.User
import kotlinx.coroutines.launch


@Composable
fun AdminScreen(
    adminViewModel: AdminViewModel = hiltViewModel()
) {

    val state = adminViewModel.state.value
    val snackbarHostState =
        remember { SnackbarHostState() } // for hosting snackbars, if I delete a intem I get a snackbar to undo the item

    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        adminViewModel.getUsers()
    }

        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Admin Panel ",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
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
                    .padding(bottom = 8.dp)
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
                            )
                            .padding(horizontal = 16.dp, vertical = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (roleOption == state.user.role),
                            onClick = { adminViewModel.onEvent(AdminEvent.SelectedRole(roleOption)) }
                        )
                        Text(
                            text = roleOption.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            Modifier.semantics {
                                this.contentDescription = "LOADING INDICATOR"
                            }
                        )
                    }
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
                                        if(undo == SnackbarResult.ActionPerformed){
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