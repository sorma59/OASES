package com.unimib.oases.ui.util.snackbar

data class SnackbarData(
    val message: String,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)