package com.unimib.oases.util

/**
 * A sealed class representing the status of a resource operation.
 *
 * This class is used to wrap the result of an operation, indicating whether it was
 * successful, resulted in an error, or is still loading.
 *
 * @param T The type of data being wrapped.
 * @property data The data associated with the resource, if available.
 * @property message An error message, if applicable.
 */
sealed class Resource<T>(var data: T? = null, val message: String? = null) {

    /**
     * Represents a successful resource operation.
     *
     * @param data The data associated with the successful operation.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents an error that occurred during a resource operation.
     *
     * @param message An error message describing the error.
     * @param data The data associated with the error, if available.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Represents a resource operation that is currently loading.
     */
    class Loading<T>(message: String? = null) : Resource<T>(message = message)

    /**
     * Represents a resource operation that has not yet started.
     */
    class None<T> : Resource<T>()
}