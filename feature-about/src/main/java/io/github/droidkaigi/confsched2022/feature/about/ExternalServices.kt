package io.github.droidkaigi.confsched2022.feature.about

import androidx.annotation.DrawableRes

sealed interface ExternalServices {
    val iconRes: Int
    val packageName: String
    val url: String
    val contentDescription: String

    data class Twitter(
        @DrawableRes
        override val iconRes: Int = R.drawable.ic_twitter,
        override val packageName: String = "com.twitter.android",
        override val url: String = "https://twitter.com/DroidKaigi",
        override val contentDescription: String = "Twitter"
    ) : ExternalServices

    data class YouTube(
        @DrawableRes
        override val iconRes: Int = R.drawable.ic_youtube,
        override val packageName: String = "com.google.android.youtube",
        override val url: String = "https://www.youtube.com/c/DroidKaigi",
        override val contentDescription: String = "YouTube"
    ) : ExternalServices

    data class Medium(
        @DrawableRes
        override val iconRes: Int = R.drawable.ic_medium,
        override val packageName: String = "com.medium.reader",
        override val url: String = "https://medium.com/droidkaigi",
        override val contentDescription: String = "Medium"
    ) : ExternalServices
}