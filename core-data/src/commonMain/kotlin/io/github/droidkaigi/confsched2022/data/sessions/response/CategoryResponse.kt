package io.github.droidkaigi.confsched2022.data.sessions.response

import kotlinx.serialization.Serializable

@Serializable
internal data class CategoryResponse(
    val id: Int,
    val sort: Int,
    val title: LocaledResponse,
    val items: List<CategoryItemResponse> = emptyList(),
)
