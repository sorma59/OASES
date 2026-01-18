package com.unimib.oases.ui.util.snackbar

import androidx.compose.material3.SnackbarDuration

data class SnackbarData(
    val message: String,
    val type: SnackbarType,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = true,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onAction: (() -> Unit)? = null,
)