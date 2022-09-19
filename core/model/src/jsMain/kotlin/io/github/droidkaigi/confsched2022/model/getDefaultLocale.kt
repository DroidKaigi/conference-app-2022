package io.github.droidkaigi.confsched2022.model

import io.github.droidkaigi.confsched2022.model.Locale.JAPAN
import io.github.droidkaigi.confsched2022.model.Locale.OTHER
import kotlinx.browser.window

public actual fun getDefaultLocale(): Locale {
    if (window.navigator.language.contains("JP", ignoreCase = true)) {
        return JAPAN
    }
    return OTHER
}
