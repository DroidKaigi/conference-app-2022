package io.github.droidkaigi.confsched2022.feature.sponsors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorPlan.Gold
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorPlan.Platinum
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorPlan.Supporter
import io.github.droidkaigi.confsched2022.strings.Strings
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Error
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Loading
import io.github.droidkaigi.confsched2022.ui.UiLoadState.Success
import kotlinx.collections.immutable.PersistentList

@Composable
fun SponsorsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SponsorsViewModel = hiltViewModel(),
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {},
    onItemClick: (url: String) -> Unit = { _ -> }
) {
    val uiModel by viewModel.uiModel
    Sponsors(
        modifier = modifier,
        uiModel = uiModel,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
        onItemClick = onItemClick
    )
}

@Composable
fun Sponsors(
    uiModel: SponsorsUiModel,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (url: String) -> Unit = { _ -> },
) {
    KaigiScaffold(
        modifier = modifier,
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
        when (uiModel.state) {
            Loading -> FullScreenLoading(Modifier.padding(innerPadding))
            is Success ->
                LazyVerticalGrid(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = innerPadding
                ) {
                    sponsorsSection(
                        sponsorItemList = uiModel.state.value,
                        onItemClick = onItemClick
                    )
                }
            is Error -> TODO()
        }
    }
}

@Composable
private fun FullScreenLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private fun LazyGridScope.sponsorsSection(
    sponsorItemList: PersistentList<SponsorItem>,
    onItemClick: (url: String) -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Spacer(modifier = Modifier.height(8.dp))
    }
    sponsorItemList.forEach { item ->
        when (item) {
            is SponsorItem.Title -> item(span = { GridItemSpan(maxLineSpan) }) {
                SponsorTitle(titleItem = item)
            }
            is SponsorItem.Sponsors -> sponsorsList(
                sponsorsItem = item,
                onItemClick = onItemClick
            )
            is SponsorItem.Divider -> item(span = { GridItemSpan(maxLineSpan) }) {
                SponsorDivider()
            }
        }
    }
}

@Composable
private fun SponsorTitle(titleItem: SponsorItem.Title) {
    Text(
        modifier = Modifier.padding(vertical = 8.dp),
        text = stringResource(resource = titleItem.value),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleLarge
    )
}

private fun LazyGridScope.sponsorsList(
    sponsorsItem: SponsorItem.Sponsors,
    onItemClick: (url: String) -> Unit
) {
    when (sponsorsItem.plan) {
        Platinum -> sponsorsGrid(
            modifier = Modifier.height(112.dp),
            sponsorsItem = sponsorsItem,
            span = { GridItemSpan(maxLineSpan) },
            onItemClick = onItemClick
        )
        Gold -> sponsorsGrid(
            modifier = Modifier.height(112.dp),
            sponsorsItem = sponsorsItem,
            onItemClick = onItemClick
        )

        Supporter -> sponsorsGrid(
            modifier = Modifier.height(72.dp),
            sponsorsItem = sponsorsItem,
            onItemClick = onItemClick
        )
    }
}

private fun LazyGridScope.sponsorsGrid(
    modifier: Modifier = Modifier,
    sponsorsItem: SponsorItem.Sponsors,
    span: (LazyGridItemSpanScope.(item: SponsorUiModel) -> GridItemSpan)? = null,
    onItemClick: (url: String) -> Unit,
) {
    items(items = sponsorsItem.sponsorList, span = span) { sponsor ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onItemClick(sponsor.link) }
        ) {
            SubcomposeAsyncImage(
                model = sponsor.logo,
                contentDescription = sponsor.name,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(12.dp),
                            ),
                    )
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SponsorDivider() {
    Divider(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outline
    )
}
