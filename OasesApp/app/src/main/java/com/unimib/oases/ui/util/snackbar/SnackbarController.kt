package com.unimib.oases.ui.util.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class SnackbarType {
    INFO, SUCCESS, ERROR
}

object SnackbarController {
    private var snackbarHostState: SnackbarHostState? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    // We store the current type so the UI can read it when drawing
    var currentType: SnackbarType = SnackbarType.INFO
        private set

    fun setHostState(hostState: SnackbarHostState) {
        snackbarHostState = hostState
    }

    fun showMessage(
        message: String,
        type: SnackbarType,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        withDismissAction: Boolean = true
    ) {
        currentType = type

        scope.launch {
            snackbarHostState?.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short,
                withDismissAction = withDismissAction
            ).run {
                when(this) {
                    SnackbarResult.ActionPerformed -> onAction?.invoke()
                    SnackbarResult.Dismissed -> Unit
                    null -> Unit
                }
            }
        }
    }
}