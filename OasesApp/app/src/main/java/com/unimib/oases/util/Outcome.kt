package com.unimib.oases.util

/**
 * A sealed class representing the outcome of an operation, which can be in one of three states:
 * Loading, Success, or Error. This is used to represent the state of asynchronous
 * operations, such as database queries, but not readings, for that view [Resource] .
 */
sealed class Outcome {
    object Loading : Outcome()
    object Success: Outcome()
    data class Error(val message: String): Outcome()
}