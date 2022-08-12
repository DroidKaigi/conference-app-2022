package io.github.droidkaigi.confsched2022.data.sessions

import io.github.droidkaigi.confsched2022.model.Session
import io.github.droidkaigi.confsched2022.model.Timetable
import io.ktor.client.HttpClient
import kotlinx.collections.immutable.persistentListOf

class SessionsApi(val httpClient: HttpClient) {
    suspend fun timetable(): Timetable {
//        return httpClient.get("xxxxx").body()
        return Timetable(
            persistentListOf(
                Session(
                    id = "test",
                    title = "title"
                )
            )
        )
    }
}
