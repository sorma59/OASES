package com.unimib.oases.ui.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SnackbarController {
    private var snackbarHostState: SnackbarHostState? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    fun setHostState(hostState: SnackbarHostState) {
        snackbarHostState = hostState
    }

    fun showMessage(
        message: String,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        scope.launch {
            snackbarHostState?.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short
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