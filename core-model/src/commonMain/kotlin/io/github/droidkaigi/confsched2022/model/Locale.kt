package io.github.droidkaigi.confsched2022.model

enum class Locale {
    JAPAN,
    OTHER
}

expect fun getDefaultLocale(): Locale
