package io.github.droidkaigi.confsched2022.feature.announcement

enum class AnnouncementType(
    val iconRes: Int,
    val titleTextColor: Long,
    val contentTextColor: Long,
) {
    Alert(
        iconRes = R.drawable.ic_baseline_error,
        titleTextColor = 0xFFFFB4AB,
        contentTextColor = 0xFFE2E3DE,
    ),
    Feedback(
        iconRes = R.drawable.ic_baseline_chat_bubble,
        titleTextColor = 0xFFE1E3DF,
        contentTextColor = 0xFFE2E3DE,
    ),
    Notification(
        iconRes = R.drawable.ic_baseline_check_box,
        titleTextColor = 0xFFE1E3DF,
        contentTextColor = 0xFFE2E3DE,
    ),
}
