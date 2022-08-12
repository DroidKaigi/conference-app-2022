package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.model.Session
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class SessionsApi(private val httpClient: HttpClient) {
    suspend fun sessions(): ImmutableList<Session> {
//        return httpClient.get("xxxxx").body()
        return persistentListOf(
            Session(
                id = "test",
                title = "title"
            )
        )
    }
}
