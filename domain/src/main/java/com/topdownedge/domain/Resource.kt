package com.topdownedge.domain


/**
 * Represents a wrapper class for handling different states of a data operation.
 * This sealed class provides type-safe handling of Success, Error, and Loading states
 * for any data type T.
 *
 * @param T The type of data being wrapped by the Resource
 */
sealed class Resource<out T>() {

    /**
     * Represents a successful state containing the actual data.
     *
     * @property data The successful result data of type T
     */
    data class Success<out T>(val data: T) : Resource<T>()

    /**
     * Represents an error state with optional data and error details.
     *
     * @property message A human-readable error message describing what went wrong
     * @property data Optional data that may be available even in an error state
     * @property exception The original exception that caused the error, if any
     */
    data class Error<out T>(
        val message: String,
        val data: T? = null,
        val exception: Throwable?
    ) : Resource<T>()

    /**
     * Represents a loading state of the operation.
     *
     * @property isLoading Boolean flag indicating whether the operation is currently loading
     */
    data class Loading<out T>(val isLoading: Boolean) : Resource<T>()

    companion object {

        fun <T> success(value: T): Resource<T> =
            Success(value)


        fun <T> failure(
            message: String,
            data: T? = null,
            exception: Throwable? = null
        ): Resource<T> =
            Error<T>(message, data, exception)


        fun <T> failure(exception: Throwable): Resource<T> =
            failure(
                exception.message ?: exception.toString(),
                null,
                exception
            )


        fun <T> loading(isLoading: Boolean): Resource<T> =
            Loading(isLoading)
    }

    /**
     * Transforms the wrapped data of this Resource from type T to type R while preserving the Resource state.
     *
     * @param transform A function that converts the wrapped data from type T to type R
     * @return A new [Resource] of type R containing the transformed data while maintaining the same state
     */
    inline fun <R> mapTo(transform: (T?) -> R?): Resource<R?> = when (this) {
        is Success -> success(transform(data))
        is Loading -> loading(isLoading)
        is Error -> failure(message, data?.let { transform(it) }, exception)
    }
}

