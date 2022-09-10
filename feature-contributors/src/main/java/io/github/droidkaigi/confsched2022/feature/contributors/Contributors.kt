package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.model.fakes
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success

@Composable
fun ContributorsScreenRoot(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    onLinkClick: (url: String, packageName: String?) -> Unit = { _, _ -> },
) {
    val viewModel = hiltViewModel<ContributorsViewModel>()
    val uiModel by viewModel.uiModel
    Contributors(uiModel, showNavigationIcon, onNavigationIconClick, onLinkClick, modifier)
}

@Composable
fun Contributors(
    uiModel: ContributorsUiModel,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String, packageName: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(id = R.string.contributors_top_app_bar_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        }
    ) {
        when (uiModel.state) {
            is Error -> TODO()
            Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            is Success -> {
                val contributors = uiModel.state.value

                LazyColumn(
                    modifier = modifier.fillMaxWidth()
                ) {
                    items(items = contributors, key = { it.id }) { contributor ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    contributor.profileUrl?.let { url ->
                                        onLinkClick(url, "com.github.android")
                                    }
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))

                            AsyncImage(
                                model = contributor.iconUrl,
                                contentDescription = contributor.username,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(60.dp)
                            )

                            Text(
                                text = contributor.username,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp)
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
                )
            ),
            showNavigationIcon = true,
            onNavigationIconClick = {},
            onLinkClick = { _, _ -> },
        )
    }
}
