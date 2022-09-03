package io.github.droidkaigi.confsched2022.designsystem.theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiScaffold(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SmallTopAppBar(
                title = {
                    Text("DroidKaigi")
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
    ) { insetPadding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(insetPadding)
        ) {
            content()
        }
    }
}
