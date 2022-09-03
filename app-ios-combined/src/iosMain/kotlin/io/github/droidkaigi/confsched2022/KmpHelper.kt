package io.github.droidkaigi.confsched2022

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(dataModule)
    }
}