package io.github.droidkaigi.confsched2022.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiTopAppBar(
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    title: (@Composable RowScope.() -> Unit),
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    trailingIcons: (@Composable RowScope.() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProvideTextStyle(MaterialTheme.typography.titleLarge) {
                    title()
                }
                Spacer(modifier = Modifier.weight(1F))
                trailingIcons?.invoke(this)
            }
        },
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(
                    onClick = onNavigationIconClick
                ) {
                    Icon(imageVector = Icons.Default.Menu, "menu")
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme
                .colorScheme
                .surfaceColorAtElevation(elevation)
        )
    )
}

@Preview
@Composable
private fun KaigiTopAppBarPreview() = KaigiTopAppBar(
    showNavigationIcon = true,
    onNavigationIconClick = {},
    title = {
        Text(
            text = "KaigiTopAppBarPreview",
        )
    }
)

@Preview
@Composable
private fun KaigiTopAppBarPreviewHiddenNavigationIcon() = KaigiTopAppBar(
    showNavigationIcon = false,
    onNavigationIconClick = {},
    title = {
        Text(
            text = "KaigiTopAppBarPreview",
        )
    }
)
