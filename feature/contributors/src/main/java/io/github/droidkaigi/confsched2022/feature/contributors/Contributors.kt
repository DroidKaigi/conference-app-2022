package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.designsystem.components.UsernameRow
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.announcement.AppErrorSnackbarEffect
import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.model.fakes
import io.github.droidkaigi.confsched2022.strings.Strings
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success

@Composable
fun ContributorsScreenRoot(
    viewModel: ContributorsViewModel = hiltViewModel(),
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    onLinkClick: (url: String, packageName: String?) -> Unit = { _, _ -> },
) {
    val uiModel by viewModel.uiModel
    Contributors(
        uiModel = uiModel,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
        onRetryButtonClick = { viewModel.onRetryButtonClick() },
        onAppErrorNotified = { viewModel.onAppErrorNotified() },
        onLinkClick = onLinkClick
    )
}

@Composable
fun Contributors(
    uiModel: ContributorsUiModel,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onRetryButtonClick: () -> Unit,
    onAppErrorNotified: () -> Unit,
    onLinkClick: (url: String, packageName: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    KaigiScaffold(
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(Strings.contributors_top_app_bar_title),
                    )
                },
            )
        }
    ) { innerPadding ->
        AppErrorSnackbarEffect(
            appError = uiModel.appError,
            snackBarHostState = snackbarHostState,
            onAppErrorNotified = onAppErrorNotified,
            onRetryButtonClick = onRetryButtonClick
        )
        Box {
            when (uiModel.state) {
                is Error -> {
                    // Do nothing
                }
                Loading -> Box(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
                is Success -> {
                    val contributors = uiModel.state.value

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = innerPadding
                    ) {
                        items(items = contributors, key = { it.id }) { contributor ->
                            UsernameRow(
                                username = contributor.username,
                                profileUrl = contributor.profileUrl,
                                iconUrl = contributor.iconUrl,
                                onLinkClick = onLinkClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContributorsPreview() {
    KaigiTheme {
        Contributors(
            uiModel = ContributorsUiModel(
                state = Success(
                    Contributor.fakes()
                ),
                appError = null,
            ),
            showNavigationIcon = true,
            onNavigationIconClick = {},
            onRetryButtonClick = {},
            onAppErrorNotified = {},
            onLinkClick = { _, _ -> },
        )
    }
}
