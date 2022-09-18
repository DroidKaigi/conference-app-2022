package io.github.droidkaigi.confsched2022.model

actual fun getDefaultLocale(): Locale =
    if (java.util.Locale.getDefault() == java.util.Locale.JAPAN) {
        Locale.JAPAN
    } else {
        Locale.OTHER
    }
