package io.github.droidkaigi.confsched2022.data.staff.response

import kotlinx.serialization.Serializable

@Serializable
data class StaffResponse(
    val status: String,
    val staff: List<Staff>,
)

@Serializable
data class Staff(
    val id: Int,
    val username: String,
    val profileUrl: String,
    val iconUrl: String,
)
