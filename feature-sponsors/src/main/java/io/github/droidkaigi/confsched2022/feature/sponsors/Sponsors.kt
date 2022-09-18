package io.github.droidkaigi.confsched2022.feature.sponsors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
fun SponsorsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SponsorsViewModel = hiltViewModel(),
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {}
) {
    val uiModel by viewModel.uiModel
    Sponsors(
        modifier = modifier,
        uiModel = uiModel,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick
    )
}

@Composable
fun Sponsors(
    modifier: Modifier = Modifier,
    uiModel: SponsorsUiModel,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(Strings.sponsors_top_app_bar_title),
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Coming Soon...")
            Spacer(modifier = Modifier.height(16.dp))
            // FIXME: apply sponsors to UI
            val isSuccessText = if (uiModel.state.getOrNull() != null) "Success" else "Failure"
            Text(text = "$isSuccessText to get Sponsors")
        }
    }
}
