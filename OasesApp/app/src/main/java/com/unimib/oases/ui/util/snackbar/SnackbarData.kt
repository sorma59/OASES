package com.unimib.oases.ui.util.snackbar

import androidx.compose.material3.SnackbarDuration

data class SnackbarData(
    val message: String,
    val type: SnackbarType,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = true,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onAction: (() -> Unit)? = null,
) {
    companion object {
        val SaveSuccess = SnackbarData(
            message = "Saved successfully",
            type = SnackbarType.SUCCESS
        )
        val SaveError = SnackbarData(
            message = "An error occurred while saving",
            type = SnackbarType.ERROR
        )
        val DeleteSuccess = SnackbarData(
            message = "Deleted successfully",
            type = SnackbarType.SUCCESS
        )
        val DeleteError = SnackbarData(
            message = "An error occurred while deleting",
            type = SnackbarType.ERROR
        )
        val GenericError = SnackbarData(
            message = "An unexpected error occurred",
            type = SnackbarType.ERROR
        )
    }
}
