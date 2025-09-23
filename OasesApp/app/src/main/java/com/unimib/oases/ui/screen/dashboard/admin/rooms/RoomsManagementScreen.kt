package com.unimib.oases.ui.screen.dashboard.admin.rooms

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unimib.oases.domain.model.AgeSpecificity
import com.unimib.oases.domain.model.Room
import com.unimib.oases.domain.model.SexSpecificity
import com.unimib.oases.ui.components.util.OutlinedDropdown
import com.unimib.oases.ui.components.util.button.DeleteButton
import com.unimib.oases.ui.components.util.button.DismissButton
import com.unimib.oases.ui.components.util.circularprogressindicator.CustomCircularProgressIndicator
import kotlinx.coroutines.launch


@Composable
fun RoomsManagementScreen(
    roomManagementViewModel: RoomsManagementViewModel = hiltViewModel(),
) {

    val state by roomManagementViewModel.state.collectAsState()

    val snackbarHostState =
        remember { SnackbarHostState() } // for hosting snackbars, if I delete a intem I get a snackbar to undo the item

    val scope = rememberCoroutineScope()

    var showDeletionDialog by remember { mutableStateOf(false) }

    var roomToDelete by remember { mutableStateOf<Room?>(null) }

    val dismissDeletionDialog = {
        showDeletionDialog = false
        roomToDelete = null
    }

    LaunchedEffect(key1 = true) {
        roomManagementViewModel.getRooms()
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
                    text = "Add a new room:",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            OutlinedTextField(
                value = state.room.name,
                onValueChange = { roomManagementViewModel.onEvent(RoomsManagementEvent.EnteredRoomName(it)) },
                label = { Text("Room") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {

//                        if (state.room.username.isBlank() || state.dise.pwHash.isBlank()) {
//                            state.error = "Username and password cannot be empty!"
//                            return@Button
//                        }

                    roomManagementViewModel.onEvent(RoomsManagementEvent.SaveRoom)

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Room")
            }


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

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Rooms (${state.rooms.size})",
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
                        items(state.rooms.size) { i ->
                            val room = state.rooms[i]
                            RoomListItem (
                                room = room,
                                onDelete = {
                                    roomToDelete = room
                                    showDeletionDialog = true
                                },
                                onClick = {
                                    roomManagementViewModel.onEvent(RoomsManagementEvent.Click(room))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeletionDialog){
        AlertDialog(
            onDismissRequest = dismissDeletionDialog,
            title = { "Delete Room" },
            text = { Text("Are you sure you want to delete this room?") },
            confirmButton = {
                DeleteButton(
                    onDelete = {
                        roomManagementViewModel.onEvent(RoomsManagementEvent.Delete(roomToDelete!!))
                        scope.launch {
                            val undo = snackbarHostState.showSnackbar(
                                message = "Deleted room ${roomToDelete?.name ?: ""}",
                                actionLabel = "UNDO"
                            )
                            if (undo == SnackbarResult.ActionPerformed) {
                                roomManagementViewModel.onEvent(RoomsManagementEvent.UndoDelete)
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


@Composable
fun RoomListItem(
    room: Room,
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
                    text = room.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete room",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}