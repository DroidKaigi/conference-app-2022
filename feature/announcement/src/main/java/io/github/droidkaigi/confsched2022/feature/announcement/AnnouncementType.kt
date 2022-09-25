package io.github.droidkaigi.confsched2022.feature.announcement

import io.github.droidkaigi.confsched2022.core.model.R.color

enum class AnnouncementType(
    val iconRes: Int,
    val titleTextColorRes: Int,
    val contentTextColor: Long,
) {
    Alert(
        iconRes = R.drawable.ic_baseline_error,
        titleTextColorRes = color.announcement_alert,
        contentTextColor = 0xFFE2E3DE,
    ),
    Feedback(
        iconRes = R.drawable.ic_baseline_chat_bubble,
        titleTextColorRes = color.announcement_feedback,
        contentTextColor = 0xFFE2E3DE,
    ),
    Notification(
        iconRes = R.drawable.ic_baseline_check_box,
        titleTextColorRes = color.announcement_notification,
        contentTextColor = 0xFFE2E3DE,
    ),
}
