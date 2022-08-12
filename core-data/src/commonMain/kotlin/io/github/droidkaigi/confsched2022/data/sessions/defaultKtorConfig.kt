package io.github.droidkaigi.confsched2022.data.sessions

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

fun HttpClientConfig<*>.defaultKtorConfig() {
    install(ContentNegotiation) {
        json()
    }
}