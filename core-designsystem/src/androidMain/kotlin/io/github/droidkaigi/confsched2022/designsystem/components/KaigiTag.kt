package io.github.droidkaigi.confsched2022.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun KaigiTag(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.16f),
    icon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(color = backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            icon()
            Spacer(Modifier.width(2.dp))
        }
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
