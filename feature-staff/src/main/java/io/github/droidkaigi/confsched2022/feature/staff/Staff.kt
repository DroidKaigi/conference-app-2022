package io.github.droidkaigi.confsched2022.feature.staff

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
import io.github.droidkaigi.confsched2022.model.Staff
import io.github.droidkaigi.confsched2022.model.fakes
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success

@Composable
fun StaffScreenRoot(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    onLinkClick: (url: String, packageName: String?) -> Unit = { _, _ -> }
) {
    val viewModel = hiltViewModel<StaffViewModel>()
    val uiModel by viewModel.uiModel
    Staff(uiModel, showNavigationIcon, onNavigationIconClick, onLinkClick, modifier)
}

@Composable
fun Staff(
    uiModel: StaffUiModel,
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
                        text = stringResource(id = R.string.staff_top_app_bar_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
            )
        }
    ) {
        when (uiModel.state) {
            is Error -> TODO()
            is Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            is Success -> {
                val staff = uiModel.state.value

                LazyColumn(
                    modifier = modifier.fillMaxWidth()
                ) {
                    items(items = staff, key = { it.id }) { staff ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    staff.profileUrl?.let { url ->
                                        onLinkClick(url, "com.github.android")
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))

                            AsyncImage(
                                model = staff.iconUrl,
                                contentDescription = staff.username,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(60.dp)
                            )

                            Text(
                                text = staff.username,
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
fun StaffPreview() {
    KaigiTheme {
        Staff(
            uiModel = StaffUiModel(
                state = Success(
                    Staff.fakes()
                )
            ),
            showNavigationIcon = true,
            onNavigationIconClick = {},
            onLinkClick = { _, _ -> },
        )
    }
}
