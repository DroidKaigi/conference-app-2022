package io.github.droidkaigi.confsched2022.modifier

import app.cash.zipline.Zipline

private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class)
@JsExport
fun prepareModifiers() {
    zipline.bind<TimetableModifier>(
        name = "sessionsModifier",
        instance = JsTimetableModifier()
    )
}
