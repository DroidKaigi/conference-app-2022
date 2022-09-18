package io.github.droidkaigi.confsched2022.data.staff.response

import kotlinx.serialization.Serializable

@Serializable
public data class StaffResponse(
    val status: String,
    val staff: List<Staff>,
)

@Serializable
public data class Staff(
    val id: Int,
    val username: String,
    val profileUrl: String,
    val iconUrl: String,
)
