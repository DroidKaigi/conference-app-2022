package io.github.droidkaigi.confsched2022.data.sessions.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.data.sessions.DataSessionsRepository
import io.github.droidkaigi.confsched2022.data.sessions.SessionsApi
import io.github.droidkaigi.confsched2022.data.sessions.SessionsDao
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SessionDataModule {

    @Provides
    @Singleton
    fun provideSessionsRepository(
        networkService: NetworkService,
        sessionsDao: SessionsDao,
        settingsDatastore: SettingsDatastore
    ): SessionsRepository {
        val sessionsApi = SessionsApi(networkService)
        return DataSessionsRepository(
            sessionsApi = sessionsApi,
            sessionsDao = sessionsDao,
            settingsDatastore = settingsDatastore
        )
    }
//    @Provides
//    fun provideSessionsApi(): SessionsApi {
//        return SessionsApi()
//    }
}
