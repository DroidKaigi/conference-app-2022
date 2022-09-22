package io.github.droidkaigi.confsched2022.feature.setting

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.components.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.setting.LanguageItems.SYSTEM_DEFAULT
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
fun SettingScreenRoot(
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {}
) {
    Setting(
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick
    )
}

@Composable
fun Setting(
    showNavigationIcon: Boolean,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit
) {
    KaigiScaffold(
        modifier = modifier,
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
            DarkModeSetting()
            LanguageSetting()
        }
    }
}

@Composable
fun DarkModeSetting() {
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
            Icon(imageVector = Icons.Default.DarkMode, contentDescription = null)
            Spacer(modifier = Modifier.width(28.dp))
            Text(text = stringResource(resource = Strings.setting_item_dark_mode))
        }
        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it }
        )
    }
}

@Composable
private fun LanguageSetting() {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { openDialog.value = true },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Language, contentDescription = null)
            Spacer(modifier = Modifier.width(28.dp))
            Text(text = stringResource(resource = Strings.setting_item_language))
        }
    }
    if (openDialog.value) {
        LanguageSettingDialog { openDialog.value = false }
    }
}

@Composable
private fun LanguageSettingDialog(onClickConfirm: () -> Unit) {
    val (selectedLocale, onLocaleSelected) =
        remember { mutableStateOf(AppCompatDelegate.getApplicationLocales()) }
    AlertDialog(
        onDismissRequest = onClickConfirm,
        title = { Text(text = stringResource(resource = Strings.setting_item_language)) },
        text = {
               LanguageSelector(
                   currentLocale = selectedLocale,
                   onLocaleSelected = onLocaleSelected
               )
        },
        confirmButton = {
            Button(
                onClick = {
                    AppCompatDelegate.setApplicationLocales(selectedLocale)
                    onClickConfirm()
                }
            ) {
                Text(text = stringResource(resource = Strings.setting_dialog_confirm))
            }
        }
    )
}

@Composable
private fun LanguageSelector(
    currentLocale: LocaleListCompat,
    onLocaleSelected: (LocaleListCompat) -> Unit
) {
    val languages = LanguageItems.values().toList()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        languages.forEach {
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = it.selected(currentLocale),
                        onClick = { onLocaleSelected(LocaleListCompat.forLanguageTags(it.tag)) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = it.selected(currentLocale),
                    onClick = { onLocaleSelected(LocaleListCompat.forLanguageTags(it.tag)) }
                )
                Text(text = it.title)
            }
        }
    }
}

@Preview
@Composable
private fun SettingPreview() {
    KaigiTheme {
        SettingScreenRoot()
    }
}

@Preview
@Composable
private fun LanguageSettingPreview() {
    KaigiTheme {
        LanguageSettingDialog {}
    }
}

internal enum class LanguageItems(
    val tag: String,
    val title: String
) {
    SYSTEM_DEFAULT("", "System Default"),
    ENGLISH("en", "English"),
    JAPANESE("ja", "日本語"),
    SIMPLIFIED_CHINESE("zh-CN", "简体中文")
}
private fun LanguageItems.selected(it: LocaleListCompat): Boolean = when (this) {
    SYSTEM_DEFAULT -> it.isEmpty
    else -> it.toLanguageTags().contains(tag)
}
