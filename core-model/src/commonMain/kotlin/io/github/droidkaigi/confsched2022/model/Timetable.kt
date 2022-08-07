package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableList

@Immutable
data class Timetable(val sessions: ImmutableList<Session>)
