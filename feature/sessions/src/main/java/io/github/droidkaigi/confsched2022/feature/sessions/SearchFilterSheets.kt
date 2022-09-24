package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale

@Composable
fun FilterDaySheet(
    selectedDay: DroidKaigi2022Day?,
    kaigiDays: List<DroidKaigi2022Day>,
    onDaySelected: (DroidKaigi2022Day, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val onDaySelectedUpdated by rememberUpdatedState(newValue = onDaySelected)

    Column(modifier = modifier) {
        TopBar(
            title = stringResource(id = Strings.search_filter_select_day.resourceId),
            onDismissClicked = onDismiss
        )

        kaigiDays.forEach { kaigiDay ->
            Row(
                modifier = Modifier
                    .clickable { onDaySelectedUpdated(kaigiDay, selectedDay == kaigiDay) }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val japanese = "ja"

                val date = kaigiDay.start.toLocalDateTime(TimeZone.currentSystemDefault())

                val year = if (Locale.getDefault().language == japanese) {
                    "${date.year}年"
                } else {
                    "${date.year}"
                }

                val month = if (Locale.getDefault().language == japanese) {
                    "${date.monthNumber}月"
                } else {
                    date.month.name.lowercase().replaceFirstChar { it.uppercase() }
                }

                val day = if (Locale.getDefault().language == japanese) {
                    "${date.dayOfMonth}日"
                } else {
                    "${date.dayOfMonth}th"
                }

                Checkbox(
                    checked = selectedDay == kaigiDay,
                    onCheckedChange = {},
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    text = "${kaigiDay.name} ($year $month $day)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FilterCategoriesSheet(
    selectedCategories: List<TimetableCategory>,
    categories: List<TimetableCategory>,
    onCategoriesSelected: (TimetableCategory, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val onCategoriesSelectedUpdated by rememberUpdatedState(newValue = onCategoriesSelected)

    Column(modifier = modifier) {

        TopBar(
            title = stringResource(id = Strings.search_filter_select_category.resourceId),
            onDismissClicked = onDismiss
        )

        LazyColumn {
            items(categories) { category ->
                Row(
                    modifier = Modifier
                        .clickable {
                            onCategoriesSelectedUpdated(
                                category,
                                selectedCategories
                                    .contains(category)
                                    .not()
                            )
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedCategories.contains(category),
                        onCheckedChange = {},
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Text(
                        text = category.title.currentLangTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
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
