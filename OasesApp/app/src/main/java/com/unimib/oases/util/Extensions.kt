package com.unimib.oases.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unimib.oases.domain.model.symptom.Symptom
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
 * Selects a symptom from a set of symptoms.
 *
 * This function is used to ensure that only one symptom from each group is selected.
 * If a symptom from a group is already selected, it will be replaced by the new symptom.
 * If no symptom from the group is selected, the new symptom will be added.
 *
 * @param symptom The symptom to select.
 * @return A new set of symptoms with the selected symptom.
 * @throws IllegalArgumentException if the symptom is not part of a symptom group.
 */
fun Set<Symptom>.select(symptom: Symptom): Set<Symptom> {

    if (symptom.group == null)
        throw IllegalArgumentException("Symptom must be part of a symptom group")

    // Create a mutable copy of the original set to modify
    val mutableSet = this.toMutableSet()

    // Remove all symptoms that belong to the same group as the symptom
    mutableSet.removeAll { it.group == symptom.group }

    // Add the symptom
    mutableSet.add(symptom)

    // Return an immutable set
    return mutableSet.toSet()
}

fun <T> List<T>.replaceAt(index: Int, element: T): List<T>{
    val list = this.toMutableList()
    list[index] = element
    return list
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