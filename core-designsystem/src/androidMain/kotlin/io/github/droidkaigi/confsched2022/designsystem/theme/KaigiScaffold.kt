package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            topBar()
        },
        bottomBar = {
            bottomBar()
        }
    ) { insetPadding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(insetPadding)
        ) {
            content()
        }
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
