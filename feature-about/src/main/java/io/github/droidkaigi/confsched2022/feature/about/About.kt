package io.github.droidkaigi.confsched2022.feature.about

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar

@Composable
fun AboutScreenRoot(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {
    About(onNavigationIconClick, modifier)
}

@Composable
fun About(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(onNavigationIconClick = onNavigationIconClick)
        }
    ) {
        Text("This is About Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    KaigiTheme {
        AboutScreenRoot()
    }
}
