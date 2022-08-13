package io.github.droidkaigi.confsched2022.model

enum class Lang {
    SYSTEM,
    JA,
    EN;
}

fun defaultLang() = if (getDefaultLocale() == Locale.JAPAN) Lang.JA else Lang.EN
