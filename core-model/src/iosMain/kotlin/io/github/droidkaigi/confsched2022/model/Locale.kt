package io.github.droidkaigi.confsched2022.model

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale

actual fun getDefaultLocale(): Locale =
    if (NSLocale.currentLocale.countryCode == "JP") {
        Locale.JAPAN
    } else {
        Locale.OTHER
    }
