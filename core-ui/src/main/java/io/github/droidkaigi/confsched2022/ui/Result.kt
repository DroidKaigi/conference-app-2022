package io.github.droidkaigi.confsched2022.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

// Credit: from now in android
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this.map<T, Result<T>> {
        Result.Success(it)
    }.onStart { emit(Result.Loading) }.catch { emit(Result.Error(it)) }
}

sealed interface LoadState<out T> {
    object Loading : LoadState<Nothing>
    data class Success<T>(val value: T) : LoadState<T>
    class Error(val value: Throwable? = null) : LoadState<Nothing>

    fun getOrNull() = if (this is Success) {
        value
    } else {
        null
    }

    fun <R> mapSuccess(transform: (value: T) -> R): LoadState<R> = when (this) {
        is Success -> Success(transform(this.value))
        is Loading -> this
        is Error -> this
    }

    fun <OtherT> combine(other: LoadState<OtherT>): LoadState<Pair<T, OtherT>> = when (this) {
        is Loading -> Loading
        is Error -> Error(value)
        is Success -> when (other) {
            is Loading -> Loading
            is Error -> Error(other.value)
            is Success -> Success(value to other.value)
        }
    }
}

fun <T> Flow<T>.asLoadState(): Flow<LoadState<T>> {
    return map<T, LoadState<T>> { LoadState.Success(it) }
        .onStart { emit(LoadState.Loading) }
        .catch { emit(LoadState.Error(it)) }
}
