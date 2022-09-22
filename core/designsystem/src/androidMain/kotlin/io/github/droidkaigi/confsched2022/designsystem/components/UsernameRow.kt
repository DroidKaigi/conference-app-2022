package io.github.droidkaigi.confsched2022.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UsernameRow(
    username: String,
    profileUrl: String?,
    iconUrl: String,
    onLinkClick: (url: String, packageName: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                profileUrl?.let { url ->
                    onLinkClick(url, "com.github.android")
                }
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        AsyncImage(
            model = iconUrl,
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
        )

        Text(
            text = username,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
