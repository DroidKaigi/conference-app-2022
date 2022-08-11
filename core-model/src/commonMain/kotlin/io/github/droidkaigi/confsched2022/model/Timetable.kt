package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Timetable(
    @Serializable(PersistentListSerializer::class) val sessions: ImmutableList<Session>
)
