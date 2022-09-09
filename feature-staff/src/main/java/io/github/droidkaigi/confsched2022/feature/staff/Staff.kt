package io.github.droidkaigi.confsched2022.feature.staff

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiScaffold
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTopAppBar
import io.github.droidkaigi.confsched2022.feature.staff.StaffUiModel.StaffState.Loaded

@Composable
fun StaffScreenRoot(
    modifier: Modifier = Modifier,
    showNavigationIcon: Boolean = true,
    onNavigationIconClick: () -> Unit = {}
) {
    val viewModel = hiltViewModel<StaffViewModel>()
    val uiModel by viewModel.uiModel
    Staff(uiModel, showNavigationIcon, onNavigationIconClick, modifier)
}

@Composable
fun Staff(
    uiModel: StaffUiModel,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KaigiScaffold(
        topBar = {
            KaigiTopAppBar(
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavigationIconClick,
                title = {
                    Text(
                        text = stringResource(id = R.string.staff_top_app_bar_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
            )
        }
    ) {
        if (uiModel.staffState !is Loaded) {
            CircularProgressIndicator()
            return@KaigiScaffold
        }
        val staff = uiModel.staffState.staff

        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(items = staff, key = { it.id }) { staff ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    AsyncImage(
                        model = staff.iconUrl,
                        contentDescription = staff.username,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .clip(CircleShape)
                    )
                    Text(
                        text = staff.username,
                        style = TextStyle(
                            fontWeight = FontWeight(500),
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}
