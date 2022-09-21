package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.strings.Strings

@Composable
fun FilterDaySheet(
    modifier: Modifier = Modifier,
    selectedDay: DroidKaigi2022Day?,
    days: List<DroidKaigi2022Day>,
    onDaySelected: (DroidKaigi2022Day) -> Unit,
    onDismiss: () -> Unit
) {
    val onDaySelectedUpdated by rememberUpdatedState(newValue = onDaySelected)

    Column(modifier = modifier) {
        TopBar(
            title = stringResource(id = Strings.search_filter_select_day.resourceId),
            onDismissClicked = onDismiss
        )

        days.forEach { day ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedDay == day,
                    onClick = { onDaySelectedUpdated(day) }
                )

                Text(
                    text = "${day.name} (${day.start})",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FilterCategoriesSheet(
    modifier: Modifier = Modifier,
    selectedCategories: List<TimetableCategory>,
    categories: List<TimetableCategory>,
    onCategoriesSelected: (TimetableCategory, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val onDaySelectedUpdated by rememberUpdatedState(newValue = onCategoriesSelected)

    Column(modifier = modifier) {

        TopBar(
            title = stringResource(id = Strings.search_filter_select_category.resourceId),
            onDismissClicked = onDismiss
        )

        LazyColumn {
            items(categories) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedCategories.contains(category),
                        onCheckedChange = { isChecked -> onDaySelectedUpdated(category, isChecked) }
                    )

                    Text(
                        text = category.title.currentLangTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    onDismissClicked: () -> Unit
) {
    Row(
        modifier = modifier.padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDismissClicked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_dismiss),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}