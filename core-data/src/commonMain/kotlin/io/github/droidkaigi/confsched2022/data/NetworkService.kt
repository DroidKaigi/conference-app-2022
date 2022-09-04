package io.github.droidkaigi.confsched2022.data

import io.github.droidkaigi.confsched2022.data.auth.AuthApi
import io.github.droidkaigi.confsched2022.model.AppError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.util.cio.ChannelReadException
import kotlinx.coroutines.TimeoutCancellationException

class NetworkService(val httpClient: HttpClient, val authApi: AuthApi) {

    suspend inline fun <reified T : Any> get(
        url: String,
        needAuth: Boolean = false,
    ): T = try {
        if (needAuth) {
            authApi.authIfNeeded()
        }
        httpClient.get(url)
            .body<T>()
    } catch (e: Throwable) {
        throw e.toAppError()
    }

    suspend inline fun <reified T> post(
        urlString: String,
        needAuth: Boolean = false,
        block: HttpRequestBuilder.() -> Unit = {},
    ): T = try {
        if (needAuth) {
            authApi.authIfNeeded()
        }
        httpClient.post(urlString, block)
            .body<T>()
    } catch (e: Throwable) {
        throw e.toAppError()
    }

    suspend inline fun <reified T> put(
        urlString: String,
        needAuth: Boolean = false,
        block: HttpRequestBuilder.() -> Unit = {},
    ): T = try {
        if (needAuth) {
            authApi.authIfNeeded()
        }
        httpClient.put(urlString, block)
            .body<T>()
    } catch (e: Throwable) {
        throw e.toAppError()
    }
}

fun Throwable.toAppError(): AppError {
    return when (this) {
        is AppError -> this
        is ResponseException ->
            return AppError.ApiException.ServerException(this)
        is ChannelReadException ->
            return AppError.ApiException.NetworkException(this)
        is TimeoutCancellationException, is SocketTimeoutException -> {
            AppError.ApiException
                .TimeoutException(this)
        }
        else -> AppError.UnknownException(this)
    }
}
