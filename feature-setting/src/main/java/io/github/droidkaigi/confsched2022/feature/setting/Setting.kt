package io.github.droidkaigi.confsched2022.feature.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
fun SettingScreenRoot(
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {}
) {
    Setting(showNavigationIcon, onNavigationIconClick)
}

@Composable
fun Setting(
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
                        text = stringResource(Strings.setting_title),
                    )
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(start = 28.dp, top = 40.dp, end = 28.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            SettingItem.values().forEach { item ->
                SettingItem(
                    icon = item.icon,
                    title = stringResource(item.titleStringRes)
                )
            }
        }
    }
}

enum class SettingItem(val titleStringRes: StringResource, val icon: ImageVector) {
    DarkMode(Strings.setting_item_dark_mode, Icons.Default.DarkMode),
    Language(Strings.setting_item_language, Icons.Default.Language)
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String
) {
    val checkedState = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(28.dp))
            Text(text = title)
        }
        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it }
        )
    }
}
