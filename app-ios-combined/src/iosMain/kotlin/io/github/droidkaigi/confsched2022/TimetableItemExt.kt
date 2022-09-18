package io.github.droidkaigi.confsched2022

import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.TimetableItem.Special

// workaround for type-casting of sealed class TimetableItem
fun TimetableItem.asSession(): Session? = this as? Session

fun TimetableItem.asSpecial(): Special? = this as? Special