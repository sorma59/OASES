package com.unimib.oases.util

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