package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private const val TransitionAnimationDuration = 300

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    isDropDown: Boolean,
    text: String,
    onClicked: () -> Unit
) {
    val transition = updateTransition(targetState = isSelected)

    val backgroundColor by transition.animateColor(
        label = "backgroundColor",
        transitionSpec = { tween(durationMillis = TransitionAnimationDuration) }
    ) {
        if (it)
            MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surface
    }

    val strokeColor by transition.animateColor(
        label = "strokeColor",
        transitionSpec = { tween(durationMillis = TransitionAnimationDuration) }
    ) {
        if (it)
            MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.outline
    }

    val contentColor by transition.animateColor(
        label = "textColor",
        transitionSpec = { tween(durationMillis = TransitionAnimationDuration) }
    ) {
        if (it)
            MaterialTheme.colorScheme.onSecondaryContainer
        else MaterialTheme.colorScheme.onSurface
    }

    val checkIconWidth by transition.animateDp(
        label = "checkIconWidth",
        transitionSpec = { tween(durationMillis = TransitionAnimationDuration) }
    ) { if (it) 16.dp else 0.dp }

    OutlinedButton(
        modifier = modifier,
        onClick = onClicked,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor
        ),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 6.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = strokeColor
        ),
        shape = MaterialTheme.shapes.small
    ) {
        if (isSelected) {
            Icon(
                modifier = Modifier.width(width = checkIconWidth),
                painter = painterResource(id = R.drawable.ic_filter_selected),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor
        )

        if (isDropDown) {
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_filter_dropdown),
                contentDescription = null,
                tint = contentColor
            )
        }
    }
}