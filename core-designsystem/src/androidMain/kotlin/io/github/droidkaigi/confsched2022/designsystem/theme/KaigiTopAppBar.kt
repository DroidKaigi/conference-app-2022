package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiTopAppBar(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcons: (@Composable RowScope.() -> Unit)? = null,
) {
    SmallTopAppBar(
        modifier = modifier,
        title = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_app),
                    contentDescription = "logo in toolbar"
                )
                Spacer(modifier = Modifier.weight(1F))
                trailingIcons?.invoke(this)
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(imageVector = Icons.Default.Menu, "menu")
            }
        }
    )
}
