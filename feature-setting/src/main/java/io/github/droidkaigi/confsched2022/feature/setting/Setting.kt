package io.github.droidkaigi.confsched2022.feature.setting

import androidx.compose.runtime.Composable

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
}