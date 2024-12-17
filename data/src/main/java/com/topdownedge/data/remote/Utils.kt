package com.topdownedge.data.remote

import com.topdownedge.domain.Resource
import de.jensklingenberg.ktorfit.Response
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.net.SocketTimeoutException

/**
 * Safely executes a Ktor request and handles common exceptions by wrapping the response
 * in a Resource object. Handles network timeouts and HTTP error codes.
 *
 * @param T Type of expected response body
 * @param request Lambda that executes the Ktor request
 * @return [Resource] containing either successful response data or error details
 */
suspend inline fun <T> ktorRequestToResult(request: suspend () -> Response<T>): Resource<T?> {

    return try {
        val response = request.invoke()
        response.toResource()

    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        Resource.failure(message = "Socket Timeout")
    } catch (e: HttpRequestTimeoutException) {
        e.printStackTrace()
        Resource.failure(message = "Http Timeout")
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.failure(e)
    }
}

/**
 * Converts a Ktor Response to a Resource object based on HTTP status codes.
 * Success for 2xx, appropriate failure messages for client (4xx) and server (5xx) errors.
 *
 * @param T Type of response body
 * @return [Resource] mapped from HTTP response status and body
 */
fun <T> Response<T>.toResource(): Resource<T?> {
    return when (this.code) {

        in 200..299 -> Resource.success(this.body())

        401 -> Resource.failure(message = "401 - Unauthorized")
        403 -> Resource.failure(message = "403 - Forbidden")
        404 -> Resource.failure(message = "404 - Not Found")

        in 400..499 -> Resource.failure(message = "$code - Client Error")

        in 500..599 -> Resource.failure(message = "$code - Server Error")

        else -> Resource.failure("$code - Unknown Network Error")
    }
}

