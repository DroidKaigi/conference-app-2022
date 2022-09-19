package io.github.droidkaigi.confsched2022.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface UiLoadState<out T> {
    object Loading : UiLoadState<Nothing>
    data class Success<T>(val value: T) : UiLoadState<T>
    class Error(val value: Throwable? = null) : UiLoadState<Nothing>

    fun getOrNull() = if (this is Success) {
        value
    } else {
        null
    }

    fun <R> mapSuccess(transform: (value: T) -> R): UiLoadState<R> = when (this) {
        is Success -> Success(transform(this.value))
        is Loading -> this
        is Error -> this
    }

    fun <OtherT> combine(other: UiLoadState<OtherT>): UiLoadState<Pair<T, OtherT>> = when (this) {
        is Loading -> Loading
        is Error -> Error(value)
        is Success -> when (other) {
            is Loading -> Loading
            is Error -> Error(other.value)
            is Success -> Success(value to other.value)
        }
    }
}

fun <T> Flow<T>.asLoadState(): Flow<UiLoadState<T>> {
    return map<T, UiLoadState<T>> { UiLoadState.Success(it) }
        .onStart { emit(UiLoadState.Loading) }
        .catch { emit(UiLoadState.Error(it)) }
}
