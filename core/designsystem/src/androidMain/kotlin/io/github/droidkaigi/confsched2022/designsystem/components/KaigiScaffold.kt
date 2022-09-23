package io.github.droidkaigi.confsched2022.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiScaffold(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            topBar()
        },
        bottomBar = {
            bottomBar()
        },
        snackbarHost = snackBarHost,
    ) { insetPadding ->
        content(insetPadding)
    }
}

@Preview
@Composable
fun KaigiScaffoldPreview() {
    KaigiTheme {
        KaigiScaffold(
            content = {},
            topBar = {}
        )
    }
}
