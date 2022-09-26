package io.github.droidkaigi.confsched2022.ui

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@Immutable
public sealed interface UiLoadState<out T> {
    public object Loading : UiLoadState<Nothing>
    public data class Success<T>(val value: T) : UiLoadState<T>
    public class Error(public val value: Throwable? = null) : UiLoadState<Nothing>

    public fun getOrNull() = if (this is Success) {
        value
    } else {
        null
    }

    public fun <R> mapSuccess(transform: (value: T) -> R): UiLoadState<R> = when (this) {
        is Success -> Success(transform(this.value))
        is Loading -> this
        is Error -> this
    }

    public fun <OtherT> combine(other: UiLoadState<OtherT>): UiLoadState<Pair<T, OtherT>> =
        when (this) {
            is Loading -> Loading
            is Error -> Error(value)
            is Success -> when (other) {
                is Loading -> Loading
                is Error -> Error(other.value)
                is Success -> Success(value to other.value)
            }
        }

    public val isError: Boolean get() = this is Error
    public fun getThrowableOrNull() = (this as? Error)?.value
}

public fun <T> Flow<T>.asLoadState(): Flow<UiLoadState<T>> {
    return map<T, UiLoadState<T>> { UiLoadState.Success(it) }
        .onStart { emit(UiLoadState.Loading) }
        .catch { emit(UiLoadState.Error(it)) }
}
