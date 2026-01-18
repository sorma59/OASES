package com.unimib.oases.ui.util.snackbar

data class SnackbarData(
    val message: String,
    val type: SnackbarType,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)