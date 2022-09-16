package io.github.droidkaigi.confsched2022.model

import kotlinx.serialization.Serializable

@Serializable
data class MultiLangText(
    val jaTitle: String,
    val enTitle: String,
) {
    val currentLangTitle get() = getByLang(defaultLang())

    fun getByLang(lang: Lang): String {
        return if (lang == Lang.JAPANESE) {
            jaTitle
        } else {
            enTitle
        }
    }

    companion object
}
