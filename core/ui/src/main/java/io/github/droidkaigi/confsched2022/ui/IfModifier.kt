package io.github.droidkaigi.confsched2022.ui

import androidx.compose.ui.Modifier

inline fun Modifier.ifTrue(value: Boolean, builder: Modifier.() -> Modifier): Modifier {
    val modifier = Modifier
    return then(if (value) modifier.builder() else modifier)
}
