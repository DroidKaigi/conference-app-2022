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
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import dev.icerock.moko.resources.StringResource
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
            LanguageSetting()
            DynamicColorSetting()
        }
    }
}

@Composable
private fun LanguageSetting(
    modifier: Modifier = Modifier
) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .clickable { openDialog.value = true }
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Language, contentDescription = null)
            Spacer(modifier = modifier.width(28.dp))
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
                Text(text = stringResource(resource = it.titleStringRes))
            }
        }
    }
}

// TODO
@Composable
private fun DynamicColorSetting(modifier: Modifier = Modifier) {
    var isDynamicColorEnabled by remember {
        mutableStateOf(true)
    }

    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Colorize, contentDescription = null)
        Text(
            text = stringResource(resource = Strings.setting_item_dynamic_color),
            modifier = Modifier.weight(1f),
        )
        Switch(
            checked = isDynamicColorEnabled,
            onCheckedChange = {
                isDynamicColorEnabled = it
            },
        )
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
    val titleStringRes: StringResource
) {
    SYSTEM_DEFAULT("", Strings.setting_language_system),
    ENGLISH("en", Strings.setting_language_english),
    JAPANESE("ja", Strings.setting_language_japanese),
    SIMPLIFIED_CHINESE("zh-CN", Strings.setting_language_simplified_chinese)
}

private fun LanguageItems.selected(it: LocaleListCompat): Boolean = when (this) {
    SYSTEM_DEFAULT -> it.isEmpty
    else -> it.toLanguageTags().contains(tag)
}
