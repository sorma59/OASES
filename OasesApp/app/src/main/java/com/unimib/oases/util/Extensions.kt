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
import kotlinx.coroutines.flow.first
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
 * Selects a symptom from a set of symptoms and removes a specified set of symptoms.
 *
 * This function first removes all symptoms specified in the `toRemove` set from the original set.
 * Then, it adds the specified `symptom` to the resulting set.
 *
 * @param symptom The symptom to select and add to the set.
 * @param toRemove A set of symptoms to remove from the original set.
 * @return A new set of symptoms with the specified `symptom` added and the `toRemove` symptoms removed.
 */
fun Set<Symptom>.selectAndRemove(symptom: Symptom, toRemove: Set<Symptom>): Set<Symptom> {

    // Create a mutable copy of the original set to modify
    val mutableSet = this.toMutableSet()

    // Remove all symptoms that belong to the same group as the symptom
    mutableSet.removeAll(toRemove)

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

/**
 * Collects the first emitted [Resource] from this Flow that is either [Resource.Success], [Resource.Error] or [Resource.NotFound].
 *
 * If the first emitted Resource is [Resource.NotFound], this function throws a [NoSuchElementException] with the
 * error message. Otherwise, if the first emitted Resource is [Resource.Error], this function throws an [Exception] with the
 * error message. Otherwise, it returns the non-null data from [Resource.Success].
 *
 * Usage example:
 * ```
 * val patient: Patient = patientRepository.getPatientById(patientId).firstSuccess()
 * ```
 *
 * This function is intended for Room or repository Flows that emit [Resource] wrappers for results.
 *
 * @throws NoSuchElementException if the first emitted Resource is [Resource.NotFound]
 * @throws Exception if the first emitted Resource is [Resource.Error]
 * @return T the data contained in the first [Resource.Success] emission
 */
suspend fun <T : Any> Flow<Resource<T>>.firstSuccess(): T {
    val result = first { it is Resource.Success || it is Resource.Error || it is Resource.NotFound }
    if (result is Resource.NotFound) throw NoSuchElementException(result.message)
    if (result is Resource.Error) throw Exception(result.message)
    return (result as Resource.Success).data
}

/**
 * Collects the first emitted [Resource] from this Flow that is either [Resource.Success], [Resource.Error] or [Resource.NotFound].
 *
 * If the first emitted Resource is [Resource.NotFound], this function returns null.
 * If the first emitted Resource is [Resource.Error], this function throws an [Exception] with the
 * error message. Otherwise, it returns the nullable data from [Resource.Success].
 *
 * Usage example:
 * ```
 * val patient: Patient = patientRepository.getPatientById(patientId).firstSuccess()
 * ```
 *
 * This function is intended for Room or repository Flows that emit [Resource] wrappers for results.
 *
 * @throws Exception if the first emitted Resource is [Resource.Error]
 * @return T? the data contained in the first [Resource.Success] emission
 */
suspend fun <T : Any> Flow<Resource<T>>.firstNullableSuccess(): T? {
    val result = first { it is Resource.Success || it is Resource.Error || it is Resource.NotFound }
    return when (result) {
        is Resource.NotFound -> null
        is Resource.Error -> throw Exception(result.message)
        is Resource.Success -> result.data
        is Resource.Loading -> throw IllegalStateException("Resource of type Loading is not expected here")
    }
}