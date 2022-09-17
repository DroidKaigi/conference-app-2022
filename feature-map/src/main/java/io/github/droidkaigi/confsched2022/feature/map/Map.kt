package io.github.droidkaigi.confsched2022.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MapScreenRoot() {
    Map()
}

@Composable
fun Map(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Map is temporarily unavailable :)",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
