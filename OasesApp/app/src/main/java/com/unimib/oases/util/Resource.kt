package com.unimib.oases.util

/**
 * A generic wrapper class that represents the state of a resource being loaded or fetched.
 *
 * It enforces strict type safety:
 * - `Resource.Success` can **never** contain a `null` value.
 * - All other states may optionally include messages or partial data.
 *
 * @param T The non-nullable type of data this resource holds.
 */
sealed class Resource<out T : Any> {

    /**
     * Represents a successful operation that produced valid, non-null data.
     *
     * This class enforces that the contained data [T] cannot be `null` at compile time.
     *
     * @param data The successfully retrieved or computed data.
     */
    data class Success<out T : Any>(val data: T) : Resource<T>()

    /**
     * Represents an error that occurred while fetching or processing a resource.
     *
     * @param message A human-readable message describing the error.
     * @param data Optional data that may still be available (e.g., cached or partial data).
     */
    data class Error<out T : Any>(
        val message: String,
        val data: T? = null
    ) : Resource<T>()

    /**
     * Represents a resource that is currently being loaded.
     *
     * @param message Optional loading message (e.g., "Loading data...").
     */
    data class Loading<out T : Any>(
        val message: String? = null
    ) : Resource<T>()

    /**
     * Represents a specific error case where the requested resource could not be found.
     *
     * @param message Optional message, defaulting to `"Resource not found"`.
     */
    data class NotFound<out T : Any>(
        val message: String? = "Resource not found"
    ) : Resource<T>()

    /**
     * Companion object providing convenient factory functions
     * for creating resource instances without exposing constructors directly.
     */
    companion object {

        /**
         * Creates a [Success] resource with guaranteed non-null data.
         *
         * @param data The successfully retrieved data.
         */
        fun <T : Any> success(data: T): Resource<T> = Success(data)

        /**
         * Creates an [Error] resource.
         *
         * @param message The error message.
         * @param data Optional data available despite the error.
         */
        fun <T : Any> error(message: String, data: T? = null): Resource<T> =
            Error(message, data)

        /**
         * Creates a [Loading] resource.
         *
         * @param message Optional loading message.
         */
        fun <T : Any> loading(message: String? = null): Resource<T> =
            Loading(message)

        /**
         * Creates a [NotFound] resource.
         *
         * @param message Optional message describing the missing resource.
         */
        fun <T : Any> notFound(message: String? = "Resource not found"): Resource<T> =
            NotFound(message)
    }
}