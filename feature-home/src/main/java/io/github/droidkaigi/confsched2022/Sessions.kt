package io.github.droidkaigi.confsched2022

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.droidkaigi.confsched2022.ui.theme.DroidKaigi2022Theme

@Composable
fun Sessions() {
    Text(text = "Hello!")
}

@Preview(showBackground = true)
@Composable
fun SessionsPreview() {
    DroidKaigi2022Theme {
        Sessions()
    }
}
