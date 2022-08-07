package io.github.droidkaigi.confsched2022.data.sessions.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.sessions.DataSessionsRepository
import io.github.droidkaigi.confsched2022.data.sessions.SessionsApi
import io.github.droidkaigi.confsched2022.model.SessionsRepository

@InstallIn(SingletonComponent::class)
@Module
class DataSessionModule {
    @Provides
    fun provideSessionsRepository(): SessionsRepository {
        return DataSessionsRepository(SessionsApi())
    }
//    @Provides
//    fun provideSessionsApi(): SessionsApi {
//        return SessionsApi()
//    }
}
