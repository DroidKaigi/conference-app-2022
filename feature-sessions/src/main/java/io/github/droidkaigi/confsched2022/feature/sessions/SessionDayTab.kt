package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day

@Composable
internal fun SessionDayTab(
    index: Int,
    day: DroidKaigi2022Day,
    selectedTab: Int,
    onTabClicked: (index: Int) -> Unit
) {
    val isSelected = selectedTab == index
    Tab(
        selected = isSelected,
        onClick = { onTabClicked(index) },
        modifier = Modifier.height(56.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.name,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(500)
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${5 + index}",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
