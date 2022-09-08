package io.github.droidkaigi.confsched2022.ui

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Platform sharing feature wrapper
 */
interface ShareManager {

    /**
     * Call platform sharing feature
     *
     * @param text share text
     */
    fun share(text: String)
}

val LocalShareManager = staticCompositionLocalOf<ShareManager> {
    error("CompositionLocal LocalShareManager not present")
}
