package io.github.droidkaigi.confsched2022.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class TimetableItemId(val value: String)
