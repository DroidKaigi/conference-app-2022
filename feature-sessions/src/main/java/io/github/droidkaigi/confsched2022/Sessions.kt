package io.github.droidkaigi.confsched2022

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.droidkaigi.confsched2022.ui.theme.DroidKaigi2022Theme

@Composable
fun Sessions() {
    val hiltViewModel = hiltViewModel<SessionsViewModel>()
    val state by hiltViewModel.state
    Text(
        text = "Hello! $state",
        modifier = Modifier.clickable {
            hiltViewModel.onToggle()
        }
    )
}
@Preview(showBackground = true)
@Composable
fun SessionsPreview() {
    DroidKaigi2022Theme {
        Sessions()
    }
}
