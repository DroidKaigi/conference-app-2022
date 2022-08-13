package io.github.droidkaigi.confsched2022.data.sessions.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.datastore.DataStoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.PreferenceDatastore
import io.github.droidkaigi.confsched2022.data.sessions.DataSessionsRepository
import io.github.droidkaigi.confsched2022.data.sessions.SessionsApi
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.ktor.client.HttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SessionDataModule {
    private val Context.dataStore by preferencesDataStore(
        name = PreferenceDatastore.NAME,
    )

    @Provides
    @Singleton
    fun provideSessionsRepository(
        httpClient: HttpClient,
        application: Application
    ): SessionsRepository {
        val preferenceDatastore = PreferenceDatastore(
            DataStoreSettings(datastore = application.dataStore)
        )
        val sessionsApi = SessionsApi(httpClient)
        return DataSessionsRepository(
            sessionsApi, preferenceDatastore
        )
    }
//    @Provides
//    fun provideSessionsApi(): SessionsApi {
//        return SessionsApi()
//    }
}
