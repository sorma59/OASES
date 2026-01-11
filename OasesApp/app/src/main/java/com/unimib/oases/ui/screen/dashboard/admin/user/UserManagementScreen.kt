package com.unimib.oases.ui.screen.dashboard.admin.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.data.local.model.Role
import com.unimib.oases.data.local.model.User
import com.unimib.oases.ui.components.util.button.DeleteButton
import com.unimib.oases.ui.components.util.button.DismissButton
import com.unimib.oases.ui.components.util.loading.LoadingOverlay
import com.unimib.oases.ui.util.ToastUtils
import kotlinx.coroutines.launch


@Composable
fun UserManagementScreen() {

    val userManagementViewModel: UserManagementViewModel = hiltViewModel()

    val state by userManagementViewModel.state.collectAsState()

    LoadingOverlay(state.isLoading)

    UserManagementContent(state, userManagementViewModel::onEvent)
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


@Composable
private fun UserManagementContent(
    state: UserManagementState,
    onEvent: (UserManagementEvent) -> Unit
) {
    val context = LocalContext.current

    val snackbarHostState =
        remember { SnackbarHostState() } // for hosting snackbars, if I delete an item I get a snackbar to undo the item

    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    var showDeletionDialog by remember { mutableStateOf(false) }

    var userToDelete by remember { mutableStateOf<User?>(null) }

    val dismissDeletionDialog = {
        showDeletionDialog = false
        userToDelete = null
    }

    LaunchedEffect(key1 = state.toastMessage) {
        state.toastMessage?.let {
            ToastUtils.showToast(context, it)
            onEvent(UserManagementEvent.OnToastShown)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
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
                    text = "Add a new user:",
                    style = MaterialTheme.typography.titleLarge
                )
            }


            OutlinedTextField(
                value = state.user.username,
                onValueChange = {
                    onEvent(
                        UserManagementEvent.EnteredUsername(
                            it
                        )
                    )
                },
                label = { Text("Username") },
                singleLine = true,
                isError = state.usernameError != null,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.usernameError != null) {
                Text(
                    text = state.usernameError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.user.pwHash,
                onValueChange = {
                    onEvent(
                        UserManagementEvent.EnteredPassword(
                            it
                        )
                    )
                },
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
                isError = state.passwordError != null,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.passwordError != null) {
                Text(
                    text = state.passwordError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Role selection radio buttons
            Text(
                text = "Select Role:",
                modifier = Modifier.fillMaxWidth()
            )


            Role.entries.forEach { roleOption ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (roleOption == state.user.role),
                            onClick = {
                                onEvent(
                                    UserManagementEvent.SelectedRole(
                                        roleOption
                                    )
                                )
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (roleOption == state.user.role),
                        onClick = {
                            onEvent(
                                UserManagementEvent.SelectedRole(
                                    roleOption
                                )
                            )
                        }
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
                    onEvent(
                        UserManagementEvent.SaveUser
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save User")
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Registered Users (${state.users.size})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

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
                                userToDelete = user
                                showDeletionDialog = true
                            },
                            onClick = {
                                onEvent(
                                    UserManagementEvent.UserClicked(
                                        user
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDeletionDialog) {
        AlertDialog(
            onDismissRequest = dismissDeletionDialog,
            title = { "Delete User" },
            text = { Text("Are you sure you want to delete this user?") },
            confirmButton = {
                DeleteButton(
                    onDelete = {
                        onEvent(UserManagementEvent.Delete(userToDelete!!))
                        scope.launch {
                            val undo = snackbarHostState.showSnackbar(
                                message = "Deleted user ${userToDelete?.username ?: ""}",
                                actionLabel = "UNDO"
                            )
                            if (undo == SnackbarResult.ActionPerformed) {
                                onEvent(UserManagementEvent.UndoDelete)
                            }
                        }
                        dismissDeletionDialog()
                    }
                )
            },
            dismissButton = {
                DismissButton(
                    onDismiss = dismissDeletionDialog,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        )
    }
}