package com.topdownedge.data.remote

import de.jensklingenberg.ktorfit.Response
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.io.IOException
import java.net.SocketTimeoutException

/**
 * Safely executes a Ktor request and handles common exceptions by wrapping the response
 * in a [Result] object. Handles network timeouts and HTTP error codes.
 *
 * @param T Type of expected response body
 * @param request Lambda that executes the Ktor request
 * @return [Result] containing either successful response data or error details
 */
suspend inline fun <T> safeApiCall(request: suspend () -> Response<T>): Result<T?> {

    return try {
        val response = request.invoke()
        response.toResult()

    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        Result.failure(IOException("Socket Timeout", e))
    } catch (e: HttpRequestTimeoutException) {
        e.printStackTrace()
        Result.failure(IOException("Http Request Timeout", e))
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(IOException("Unknown Error", e))
    }
}

/**
 * Converts a Ktor Response to a Resource object based on HTTP status codes.
 * Success for 2xx, appropriate failure messages for client (4xx) and server (5xx) errors.
 *
 * @param T Type of response body
 * @return [Result] mapped from HTTP response status and body
 */
fun <T> Response<T>.toResult(): Result<T?> {


    return when (this.code) {

        in 200..299 -> Result.success(this.body())

        401 -> Result.failure(IOException("401 - Unauthorized"))
        403 -> Result.failure(IOException("403 - Forbidden"))
        404 -> Result.failure(IOException("404 - Not Found"))

        in 400..499 -> Result.failure(IOException("$code - Client Error"))

        in 500..599 -> Result.failure(IOException("$code - Server Error"))

        else -> Result.failure(IOException("$code - Unknown Network Error"))
    }
}

