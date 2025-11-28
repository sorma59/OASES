package com.unimib.oases.ui.components.util.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteButton(
    text: String = "Delete",
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onDelete,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )

        Text(text)
    }
}