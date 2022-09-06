package io.github.droidkaigi.confsched2022.feature.about

enum class ExternalServices(
    val iconRes: Int,
    val packageName: String,
    val url: String,
    val contentDescription: String,
) {
    Twitter(
        iconRes = R.drawable.ic_twitter,
        packageName = "com.twitter.android",
        url = "https://twitter.com/DroidKaigi",
        contentDescription = "Twitter",
    ),
    Youtube(
        iconRes = R.drawable.ic_youtube,
        packageName = "com.google.android.youtube",
        url = "https://www.youtube.com/c/DroidKaigi",
        contentDescription = "YouTube",
    ),
    Medium(
        iconRes = R.drawable.ic_medium,
        packageName = "com.medium.reader",
        url = "https://medium.com/droidkaigi",
        contentDescription = "Medium",
    ),
}
