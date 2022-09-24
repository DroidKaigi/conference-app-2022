package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.material3.SnackbarDuration.Long
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.model.AppError
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
public fun AppErrorSnackbarEffect(
    appError: AppError?,
    snackBarHostState: SnackbarHostState,
    onAppErrorNotified: () -> Unit,
    onRetryButtonClick: () -> Unit
) {
    val errorMessage = stringResource(Strings.error_common_message)
    val retryMessage = stringResource(Strings.error_common_retry)
    LaunchedEffect(appError) {
        if (appError != null) {
            val snackbarResult = snackBarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = retryMessage,
                duration = Long
            )
            onAppErrorNotified()
            if (snackbarResult == ActionPerformed) onRetryButtonClick()
        }
    }
}
