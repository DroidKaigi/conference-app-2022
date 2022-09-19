package io.github.droidkaigi.confsched2022.model

public enum class Locale {
    JAPAN,
    OTHER
}

public expect fun getDefaultLocale(): Locale
