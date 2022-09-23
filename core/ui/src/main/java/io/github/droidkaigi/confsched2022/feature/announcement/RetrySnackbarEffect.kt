package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
public fun RetrySnackbarEffect(
    retrySuggestion: Boolean,
    snackBarHostState: SnackbarHostState,
    onRetryDismissed: () -> Unit,
    onRetryButtonClick: () -> Unit
) {
    val errorMessage = stringResource(Strings.error_common_message)
    val retryMessage = stringResource(Strings.error_common_retry)
    LaunchedEffect(retrySuggestion) {
        if (retrySuggestion) {
            val snackbarResult = snackBarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = retryMessage,
                duration = Indefinite
            )
            onRetryDismissed()
            if (snackbarResult == ActionPerformed) onRetryButtonClick()
        }
    }
}
