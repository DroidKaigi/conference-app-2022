package io.github.droidkaigi.confsched2022.ui

import androidx.compose.ui.Modifier

inline fun Modifier.ifTrue(value: Boolean, builder: () -> Modifier): Modifier {
    return then(if (value) builder() else Modifier)
}
