package io.github.droidkaigi.confsched2022.zipline

import app.cash.zipline.Zipline
import kotlinx.coroutines.ExperimentalCoroutinesApi

private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class, ExperimentalCoroutinesApi::class)
@JsExport
fun prepareModifiers() {
    zipline.bind<TimetableModifier>(
        name = "sessionsModifier",
        instance = JsTimetableModifier()
    )
}
