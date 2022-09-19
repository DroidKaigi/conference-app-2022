package io.github.droidkaigi.confsched2022.model

import kotlinx.serialization.Serializable

@Serializable
public data class TimetableCategory(
    val id: Int,
    val title: MultiLangText,
)
