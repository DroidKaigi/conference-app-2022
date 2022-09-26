package io.github.droidkaigi.confsched2022.ui

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
