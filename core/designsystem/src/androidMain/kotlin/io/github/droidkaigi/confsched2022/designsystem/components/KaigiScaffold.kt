package io.github.droidkaigi.confsched2022.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun KaigiScaffold(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            topBar()
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackBarData -> KaigiSnackBar(snackBarData) }
            )
        },
        bottomBar = {
            bottomBar()
        },
    ) { insetPadding ->
        content(insetPadding)
    }
}

@Composable
public fun KaigiSnackBar(
    snackBarData: SnackbarData
) {
    Snackbar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        actionColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        snackbarData = snackBarData
    )
}

@Preview
@Composable
private fun KaigiScaffoldPreview() {
    KaigiTheme {
        KaigiScaffold(
            content = {},
            topBar = {}
        )
    }
}
