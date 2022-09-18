package io.github.droidkaigi.confsched2022.zipline.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.zipline.SessionsZipline
import io.github.droidkaigi.confsched2022.zipline.SessionsZiplineImpl
import okhttp3.OkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
public class SessionsZiplineModule {

    @Provides
    @Singleton
    internal fun provideSessionsZipline(okHttpClient: OkHttpClient): SessionsZipline {
        return SessionsZiplineImpl(okHttpClient)
    }
}
