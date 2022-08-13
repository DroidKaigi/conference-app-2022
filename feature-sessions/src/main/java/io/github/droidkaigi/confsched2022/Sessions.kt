package io.github.droidkaigi.confsched2022

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.droidkaigi.confsched2022.designsystem.theme.DroidKaigiTheme
import io.github.droidkaigi.confsched2022.modifier.SessionsUiModel.SessionsState.Loaded
import io.github.droidkaigi.confsched2022.modifier.SessionsUiModel.SessionsState.Loading

@Composable
fun Sessions() {
    val hiltViewModel = hiltViewModel<SessionsViewModel>()
    val state by hiltViewModel.state
    Column(modifier = Modifier.padding(32.dp)) {
        when (val sessionState = state.sessionsState) {
            is Loaded -> {
                val timetable = sessionState.timetable
                for (session in timetable.timetableItems) {
                    // to LazyColum
                    Text(
                        text = state.toString(),
                        modifier = Modifier.clickable {
                            hiltViewModel.onFavoriteToggle(
                                sessionId = session.id,
                            )
                        }
                    )
                }
            }
            Loading -> CircularProgressIndicator()
        }
        Text(
            text = "Filter is ${if (state.isFilterOn) "ON" else "OFF"}",
            modifier = Modifier.clickable {
                hiltViewModel.onToggleFilter()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SessionsPreview() {
    DroidKaigiTheme {
        Sessions()
    }
}
