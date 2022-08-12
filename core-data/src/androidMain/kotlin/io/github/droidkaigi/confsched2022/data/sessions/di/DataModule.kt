package io.github.droidkaigi.confsched2022.data.sessions.di

import PreferenceDatastore
import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.datastore.DataStoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.sessions.DataSessionsRepository
import io.github.droidkaigi.confsched2022.data.sessions.SessionsApi
import io.github.droidkaigi.confsched2022.data.sessions.defaultKtorConfig
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import javax.inject.Singleton
import okhttp3.OkHttpClient

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    private val Context.dataStore by preferencesDataStore(
        name = PreferenceDatastore.NAME,
    )

    @Provides
    @Singleton
    fun provideSessionsRepository(okHttpClient: OkHttpClient, application: Application): SessionsRepository {
        val httpClient = HttpClient(OkHttp) {
            engine {
                config {
                    preconfigured = okHttpClient
                }
            }
            defaultKtorConfig()
        }
        val preferenceDatastore = PreferenceDatastore(
            DataStoreSettings(datastore = application.dataStore)
        )
        val sessionsApi = SessionsApi(httpClient)
        return DataSessionsRepository(
            sessionsApi, preferenceDatastore
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
//    @Provides
//    fun provideSessionsApi(): SessionsApi {
//        return SessionsApi()
//    }
}
