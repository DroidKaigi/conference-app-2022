package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public fun HttpClientConfig<*>.defaultKtorConfig(
    settingsDatastore: SettingsDatastore,
) {
    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                isLenient = true
                allowSpecialFloatingPointValues = true
                allowStructuredMapKeys = true
                prettyPrint = false
                useArrayPolymorphism = false
                ignoreUnknownKeys = true
            }
        )
    }

    defaultRequest {
        headers {
            settingsDatastore.idToken.value?.let {
                set("Authorization", "Bearer $it")
            }
        }
    }
}
