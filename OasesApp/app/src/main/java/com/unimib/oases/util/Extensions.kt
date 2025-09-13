package com.unimib.oases.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

fun <T> Flow<T>.debounce(timeoutMillis: Long): Flow<T> = channelFlow {
    var lastValue: T? = null
    var job: Job? = null

    this@debounce.collect { value ->
        lastValue = value
        job?.cancel() // Cancel the previous job to reset the timer

        job = launch {
            delay(timeoutMillis)
            lastValue?.let { send(it) } // Emit the last value after the delay
        }
    }
}

fun <T> Set<T>.toggle(element: T): Set<T> {
    return if (this.contains(element)) {
        this.minus(element)
    } else {
        this.plus(element)
    }
}

/**
* This function makes the receiver react to the keyboard being shown or hidden.
* It calculates the bottom inset of the keyboard and applies it to the receiver.
* To apply after scrollable.
 */

@Composable
fun Modifier.reactToKeyboardAppearance(): Modifier {
    val bottomInset = WindowInsets.ime
        .union(WindowInsets.navigationBars)
        .asPaddingValues()
        .calculateBottomPadding()

    return this.padding(bottom = bottomInset)
}